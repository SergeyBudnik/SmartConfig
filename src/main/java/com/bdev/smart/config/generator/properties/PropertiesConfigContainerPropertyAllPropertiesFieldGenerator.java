package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.property.AllProperties;
import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ClassField;
import net.sourceforge.jenesis4java.InnerClass;
import net.sourceforge.jenesis4java.VirtualMachine;

class PropertiesConfigContainerPropertyAllPropertiesFieldGenerator {
    static void generate(
            VirtualMachine vm,
            InnerClass dimensionPropertyClass,
            AllProperties allProperties
    ) {
        ClassField allPropertiesField = dimensionPropertyClass.newField(
                vm.newType("List<SmartConfigValue>"),
                "ALL_PROPERTIES"
        );

        allPropertiesField.setAccess(Access.PRIVATE);
        allPropertiesField.isFinal(true);

        StringBuilder expression = new StringBuilder("Arrays.asList(");

        for (String propertyName : allProperties.getAllProperties().keySet()) {
            expression.append(propertyName);
            expression.append(", ");
        }

        expression.deleteCharAt(expression.length() - 1);
        expression.deleteCharAt(expression.length() - 1);

        expression.append(")");

        allPropertiesField.setExpression(vm.newVar(expression.toString()));
    }
}
