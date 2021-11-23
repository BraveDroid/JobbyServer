package com.bravedroid.jobby.auth.domain.validators

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<PasswordConstraint?, String?> {
    private companion object {
        private const val NON_INITIALIZED = -1
    }

    private var minLength = NON_INITIALIZED
    private var maxLength = NON_INITIALIZED

    override fun initialize(passwordConstraint: PasswordConstraint?) {
        super.initialize(passwordConstraint)
        passwordConstraint?.let {
            minLength = passwordConstraint.minLength
            maxLength = passwordConstraint.maxLength
        }
    }

    override fun isValid(
        contactField: String?,
        cxt: ConstraintValidatorContext,
    ): Boolean {
        return contactField != null &&
                contactField.length >= minLength &&
                contactField.length <= maxLength
    }
}
