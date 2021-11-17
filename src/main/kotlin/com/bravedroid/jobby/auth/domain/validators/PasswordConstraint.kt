package com.bravedroid.jobby.auth.domain.validators
import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [PasswordValidator::class])

@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD,
)

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PasswordConstraint(
        val message: String = "Invalid password format",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<*>> = [],
        val  minLength: Int = 8,
        val  maxLength: Int = 20,
)
