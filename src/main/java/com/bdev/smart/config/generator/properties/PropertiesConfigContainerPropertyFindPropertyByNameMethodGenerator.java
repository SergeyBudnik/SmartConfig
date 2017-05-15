package com.bdev.smart.config.generator.properties;

import net.sourceforge.jenesis4java.*;

class PropertiesConfigContainerPropertyFindPropertyByNameMethodGenerator {
    static void generate(
            VirtualMachine vm,
            InnerClass dimensionPropertyClass
    ) {
        ClassMethod findPropertyByNameMethod = dimensionPropertyClass.newMethod(
                vm.newType("<T> Optional<SmartConfigValue<T>>"),
                "findPropertyByName"
        );

        findPropertyByNameMethod.setAccess(Access.PUBLIC);
        findPropertyByNameMethod.isFinal(true);

        findPropertyByNameMethod.addParameter(vm.newType("String"), "propertyName");

        For allPropertiesIteration = findPropertyByNameMethod.newFor();

        allPropertiesIteration.addInit(vm.newVar("int i = 0"));
        allPropertiesIteration.setPredicate(vm.newVar("i < ALL_PROPERTIES.size()"));
        allPropertiesIteration.addUpdate(vm.newVar("i++"));

        If allPropertiesIterationIf = allPropertiesIteration
                .newIf(vm.newVar("ALL_PROPERTIES.get(i).isNameSuitable(propertyName)"));

        allPropertiesIterationIf
                .newReturn()
                .setExpression(vm.newVar("Optional.of(ALL_PROPERTIES.get(i))"));

        findPropertyByNameMethod.newReturn().setExpression(vm.newVar("Optional.empty()"));
    }
}
