package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.dimension.DimensionValue;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import net.sourceforge.jenesis4java.*;

public class SmartConfigPropertiesGenerator {
    public static void generate(VirtualMachine vm, String rootPath, String rootPackage, ConfigInfo configInfo) {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(rootPackage);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.COLLECTIONS_IMPORT);
        unit.addImport(SmartConfigImports.ARRAYS_IMPORT);
        unit.addImport(SmartConfigImports.OPTIONAL_IMPORT);

        unit.addImport(SmartConfigImports.SMART_CONFIG_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);

        PackageClass smartConfigProperties = unit.newClass("SmartConfigProperties");

        smartConfigProperties.setAccess(Access.PUBLIC);

        for (Point point : configInfo.getSpaceInfo().getPoints()) {
            Field smartConfigPropertiesInstance = smartConfigProperties.newField(
                    vm.newType(point.getName() + "SmartConfig"),
                    point.getName() + "SmartConfig"
            );

            smartConfigPropertiesInstance.setAccess(Access.PRIVATE);
            smartConfigPropertiesInstance.isStatic(true);

            smartConfigPropertiesInstance.setExpression(vm.newVar(
                    "new " + point.getName() + "SmartConfig" + "()"
            ));
        }

        generateGetConfigMethod(vm, smartConfigProperties, configInfo);

        unit.encode();
    }

    private static void generateGetConfigMethod(
            VirtualMachine vm,
            PackageClass smartConfigProperties,
            ConfigInfo configInfo
    ) {
        ClassMethod getConfigMethod = smartConfigProperties.newMethod(
                vm.newType("SmartConfig"),
                "getConfig"
        ); {
            getConfigMethod.setAccess(Access.PUBLIC);
            getConfigMethod.isStatic(true);
        }

        for (Dimension dimension: configInfo.getSpaceInfo().getSpace().getDimensions()) {
            getConfigMethod.addParameter(vm.newType("String"), dimension.getName());
        }

        for (Point point: configInfo.getSpaceInfo().getPoints()) {
            Expression e = null;

            for (Dimension dimension: point.getLocation().keySet()) {
                DimensionValue dimensionValue = point.getLocation().get(dimension);

                Invoke invoke = vm.newInvoke(dimension.getName(), "equals");

                invoke.addArg(dimensionValue.getName());

                if (e == null) {
                    e = invoke;
                } else {
                    e = vm.newBinary(Binary.AND, e, invoke);
                }
            }

            If chooseConfigIf = getConfigMethod.newIf(e);

            chooseConfigIf.newReturn().setExpression(vm.newVar(
                    point.getName() + "SmartConfig"
            ));
        }

        getConfigMethod.newThrow(vm.newVar("new RuntimeException()"));
    }
}
