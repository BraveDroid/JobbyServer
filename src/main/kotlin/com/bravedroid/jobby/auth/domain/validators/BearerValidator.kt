package com.bravedroid.jobby.auth.domain.validators

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class BearerValidator : ConstraintValidator<BearerConstraint?, String?> {

    override fun isValid(
            contactField: String?,
            cxt: ConstraintValidatorContext,
    ): Boolean {
        return contactField != null &&
                contactField.startsWith("Bearer ")
    }
}
