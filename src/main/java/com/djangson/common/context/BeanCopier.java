package com.djangson.common.context;

import com.djangson.common.util.LoggerUtil;
import com.djangson.common.util.StringUtil;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.Local;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.ProtectionDomain;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: Java对象浅拷贝工具
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/12/30 17:55
 */
public abstract class BeanCopier {

    /**
     * 实例化拷贝工具对象
     * @param source
     * @param target
     * @return
     */
    public static BeanCopier create(Class source, Class target) {
        return new Generator(source, target).create(StringUtil.concat(source.getName(), ";", target.getName()));
    }

    public static <T> Converter createConverter(Class<?> resourceClass, Class<T> targetClass) {
        if (Map.class.isAssignableFrom(resourceClass)) {
            return new BeanCopierConverter();
        }
        return null;
    }

    /**
     * 对象拷贝
     * @param source
     * @param target
     * @param converter
     */
    abstract public void copy(Object source, Object target, Converter converter);

    /**
     * 处理链式调用问题
     * @param codeEmitter
     * @param writeMethodInfo
     * @param writeMethod
     */
    private static void invokeWrite(CodeEmitter codeEmitter, MethodInfo writeMethodInfo, Method writeMethod) {

        // 返回值，判断 链式 bean
        Class<?> returnType = writeMethod.getReturnType();
        codeEmitter.invoke(writeMethodInfo);

        // 链式 bean，有返回值需要 pop
        if (!returnType.equals(Void.TYPE)) {
            codeEmitter.pop();
        }
    }

    /**
     * 类生成器
     */
    public static class Generator extends AbstractClassGenerator {

        private Class source;
        private Class target;

        Generator(Class source, Class target) {
            super(new Source(BeanCopier.class.getName()));
            this.source = source;
            this.target = target;
        }

        @Override
        public BeanCopier create(Object key) {
            return (BeanCopier) super.create(key);
        }

        @Override
        protected ClassLoader getDefaultClassLoader() {
            return target.getClassLoader(); // 保证生成的代理对象和目标对象使用同一个 ClassLoader
        }

        @Override
        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(source);
        }

        @Override
        protected Object firstInstance(Class type) {
            return BeanUtils.instantiateClass(type);
        }

        @Override
        protected Object nextInstance(Object instance) {
            return instance;
        }

        @Override
        public void generateClass(ClassVisitor classVisitor) {

            // 获取源对象、目标对象类型
            Type sourceType = Type.getType(source);
            Type targetType = Type.getType(target);

            // 开始生成代理类
            ClassEmitter classEmitter = new ClassEmitter(classVisitor);
            classEmitter.begin_class(Constants.V1_8, Constants.ACC_PUBLIC, getClassName(), TypeUtils.parseType(BeanCopier.class.getName()), null, Constants.SOURCE_FILE);

            // 设置构造函数
            EmitUtils.null_constructor(classEmitter);
            CodeEmitter codeEmitter = classEmitter.begin_method(Constants.ACC_PUBLIC, new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, TypeUtils.parseType("org.springframework.cglib.core.Converter")}), null);

            // Map 单独处理
            if (!Map.class.isAssignableFrom(source)) {
                generateClass(classEmitter, codeEmitter, sourceType, targetType);
            } else {
                generateClassForMap(classEmitter, codeEmitter, sourceType, targetType);
            }
        }

        /**
         * 生成类文件
         * @param classEmitter
         * @param codeEmitter
         * @param sourceType
         * @param targetType
         */
        private void generateClass(ClassEmitter classEmitter, CodeEmitter codeEmitter, Type sourceType, Type targetType) {

            // 获取类的源的Getter（包括父类）
            List<PropertyDescriptor> getterList = new ArrayList<>();
            Class sourceParent = source;
            do {
                getterList.addAll(Arrays.asList(getBeanGetters(source)));
                sourceParent = sourceParent.getSuperclass();
            } while (sourceParent != null);

            // 获取目标对象的Setter（包括父类）
            List<PropertyDescriptor> setterList = new ArrayList<>();
            Class targetParent = target;
            do {
                setterList.addAll(Arrays.asList(getBeanSetters(targetParent)));
                targetParent = targetParent.getSuperclass();
            } while (targetParent != null);

            // 将Getter列表转换成Map
            Map<String, PropertyDescriptor> getterMap = new HashMap<>();
            for (PropertyDescriptor getter : getterList) {
                getterMap.put(getter.getName(), getter);
            }

            // 生成入口
            Local targetLocal = codeEmitter.make_local();
            Local sourceLocal = codeEmitter.make_local();
            codeEmitter.load_arg(0);
            codeEmitter.checkcast(sourceType);
            codeEmitter.store_local(sourceLocal);
            codeEmitter.load_arg(1);
            codeEmitter.checkcast(targetType);
            codeEmitter.store_local(targetLocal);

            // 遍历循环目标对象Setter，生成方法
            for (PropertyDescriptor setter : setterList) {

                // 查找Setter对应的 Getter，不存在则忽略
                PropertyDescriptor getter = getterMap.get(setter.getName());
                if (getter == null) {
                    continue;
                }

                // 生成新的Getter、Setter方法
                MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                Method writeMethod = setter.getWriteMethod();
                MethodInfo write = ReflectUtils.getMethodInfo(writeMethod);
                Type returnType = read.getSignature().getReturnType();
                Type setterType = write.getSignature().getArgumentTypes()[0];
                Class<?> getterPropertyType = getter.getPropertyType();
                Class<?> setterPropertyType = setter.getPropertyType();

                // 判断类型是否一致
                if (ClassUtils.isAssignable(getterPropertyType, setterPropertyType)) {
                    codeEmitter.load_local(targetLocal);
                    codeEmitter.load_local(sourceLocal);
                    codeEmitter.invoke(read);
                    boolean getterIsPrimitive = getterPropertyType.isPrimitive();
                    boolean setterIsPrimitive = setterPropertyType.isPrimitive();

                    // 如果 get 为原始类型，需要装箱
                    if (getterIsPrimitive && !setterIsPrimitive) {
                        codeEmitter.box(returnType);
                    }
                    // 如果 set 为原始类型，需要拆箱
                    if (!getterIsPrimitive && setterIsPrimitive) {
                        codeEmitter.unbox_or_zero(setterType);
                    }

                    // 构造 set 方法
                    invokeWrite(codeEmitter, write, writeMethod);
                }
            }
            codeEmitter.return_value();
            codeEmitter.end_method();
            classEmitter.end_class();
        }

        /**
         * 生成类文件
         * @param classEmitter
         * @param codeEmitter
         * @param sourceType
         * @param targetType
         */
        private void generateClassForMap(ClassEmitter classEmitter, CodeEmitter codeEmitter, Type sourceType, Type targetType) {

            // 获取目标对象的Setter（包括父类）
            List<PropertyDescriptor> setterList = new ArrayList<>();
            Class targetParent = target;
            do {
                setterList.addAll(Arrays.asList(getBeanSetters(targetParent)));
                targetParent = targetParent.getSuperclass();
            } while (targetParent != null);

            // 入口变量
            Local targetLocal = codeEmitter.make_local();
            Local sourceLocal = codeEmitter.make_local();
            codeEmitter.load_arg(0);
            codeEmitter.checkcast(sourceType);
            codeEmitter.store_local(sourceLocal);
            codeEmitter.load_arg(1);
            codeEmitter.checkcast(targetType);
            codeEmitter.store_local(targetLocal);

            Type mapBox = Type.getType(Object.class);

            for (PropertyDescriptor setter : setterList) {
                String propName = setter.getName();

                Method writeMethod = setter.getWriteMethod();
                MethodInfo write = ReflectUtils.getMethodInfo(writeMethod);
                Type setterType = write.getSignature().getArgumentTypes()[0];

                codeEmitter.load_local(targetLocal);
                codeEmitter.load_local(sourceLocal);
                codeEmitter.push(propName);

                // 执行 map get
                codeEmitter.invoke_interface(TypeUtils.parseType(Map.class.getName()), TypeUtils.parseSignature("Object get(Object)"));

                // box 装箱，避免 array[] 数组问题
                codeEmitter.box(mapBox);

                // 生成变量
                Local local = codeEmitter.make_local();
                codeEmitter.store_local(local);
                codeEmitter.load_local(local);

                // 先判断 不为null，然后做类型判断
                Label label = codeEmitter.make_label();
                codeEmitter.ifnull(label);
                EmitUtils.load_class(codeEmitter, setterType);
                codeEmitter.load_local(local);
                codeEmitter.invoke_static(TypeUtils.parseType(ClassUtils.class.getName()), TypeUtils.parseSignature("boolean isAssignableValue(Class, Object)"), false);

                // 返回值，判断 链式 bean
                Class<?> returnType = writeMethod.getReturnType();
                Label label2 = new Label();
                codeEmitter.if_jump(Opcodes.IFEQ, label2);
                codeEmitter.load_local(targetLocal);
                codeEmitter.load_local(local);
                codeEmitter.unbox_or_zero(setterType);
                codeEmitter.invoke(write);
                if (!returnType.equals(Void.TYPE)) {
                    codeEmitter.pop();
                }
                codeEmitter.goTo(label);
                codeEmitter.visitLabel(label2);
                codeEmitter.load_local(targetLocal);
                codeEmitter.load_arg(2);
                codeEmitter.load_local(local);
                EmitUtils.load_class(codeEmitter, setterType);
                codeEmitter.push(propName);
                codeEmitter.invoke_interface(TypeUtils.parseType("org.springframework.cglib.core.Converter"), TypeUtils.parseSignature("Object convert(Object, Class, Object)"));
                codeEmitter.unbox_or_zero(setterType);
                codeEmitter.invoke(write);

                // 返回值，判断链式Bean
                if (!returnType.equals(Void.TYPE)) {
                    codeEmitter.pop();
                }

                codeEmitter.visitLabel(label);
            }
            codeEmitter.return_value();
            codeEmitter.end_method();
            classEmitter.end_class();
        }
    }

    public static class BeanCopierConverter implements Converter {

        /**
         * 类型转换
         * @param source
         * @param target
         * @param fieldName
         * @return
         */
        @Override
        public Object convert(Object source, Class target, final Object fieldName) {
            try {

                // 若值为NULL或者类型一致，则无需转换
                if (source == null || source.getClass() == target) {
                    return source;
                }

                // 时间戳转为LocalDateTime
                if (source instanceof Number && target.getName().equals("java.time.LocalDateTime")) {
                    return Instant.ofEpochMilli(((Number) source).longValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                }

                // 数字互相转换
                if (source instanceof Number && Number.class.isAssignableFrom(target)) {
                    if (target.getName().equals("java.lang.Long")) {
                        return ((Number) source).longValue();
                    }
                    if (target.getName().equals("java.lang.Integer")) {
                        return ((Number) source).intValue();
                    }
                    if (target.getName().equals("java.lang.Short")) {
                        return ((Number) source).shortValue();
                    }
                    if (target.getName().equals("java.lang.Byte")) {
                        return ((Number) source).byteValue();
                    }
                    if (target.getName().equals("java.lang.Double")) {
                        return ((Number) source).doubleValue();
                    }
                    if (target.getName().equals("java.lang.Float")) {
                        return ((Number) source).floatValue();
                    }
                    if (target.getName().equals("java.math.BigDecimal")) {
                        return BigDecimal.valueOf(((Number) source).floatValue());
                    }
                }

                return source;

            } catch (Throwable e) {
                LoggerUtil.error("参数类型转换异常！", e);
                return null;
            }
        }
    }

    /**
     * 获取类所有可读属性
     * @param clazz
     * @return
     */
    private static PropertyDescriptor[] getBeanGetters(Class clazz) {

        // 获取所有的属性方法
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);

        // 过滤出可读属性
        List<PropertyDescriptor> propertyDescriptorList = new ArrayList<>(propertyDescriptors.length);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getReadMethod() != null) {
                propertyDescriptorList.add(propertyDescriptor);
            }
        }
        return propertyDescriptorList.toArray(new PropertyDescriptor[propertyDescriptorList.size()]);
    }

    /**
     * 获取类所有的可写属性
     * @param clazz
     * @return
     */
    private static PropertyDescriptor[] getBeanSetters(Class clazz) {

        // 获取所有的属性方法
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);

        // 过滤出可写属性
        List<PropertyDescriptor> propertyDescriptorList = new ArrayList<>(propertyDescriptors.length);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null) {
                propertyDescriptorList.add(propertyDescriptor);
            }
        }

        return propertyDescriptorList.toArray(new PropertyDescriptor[propertyDescriptorList.size()]);
    }

    /**
     * 获取类的所有属性
     * @param clazz
     * @return
     */
    private static PropertyDescriptor[] getPropertyDescriptors(Class clazz) {
        try {
            return BeanUtils.getPropertyDescriptors(clazz);
        } catch (BeansException ex) {
            throw new CodeGenerationException(ex);
        }
    }
}
