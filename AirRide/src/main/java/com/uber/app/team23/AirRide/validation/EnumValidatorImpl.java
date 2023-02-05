package com.uber.app.team23.AirRide.validation;

import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, VehicleEnum> {

    List<String> valueList = null;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueList = Stream.of(constraintAnnotation.enumClazz().getEnumConstants())
                .map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(VehicleEnum vehicleEnum, ConstraintValidatorContext constraintValidatorContext) {
        return valueList.contains(String.valueOf(vehicleEnum).toUpperCase());
    }

}
