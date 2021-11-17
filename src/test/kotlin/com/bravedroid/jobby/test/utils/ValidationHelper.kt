package com.bravedroid.jobby.test.utils

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

class ValidationHelper {
    private var validator: Validator

    init {
        val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.validator
        validatorFactory.close()
    }

    fun validate(any: Any): MutableSet<ConstraintViolation<Any>> = validator.validate(any)
}
