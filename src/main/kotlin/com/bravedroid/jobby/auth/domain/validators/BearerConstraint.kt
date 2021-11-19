package com.bravedroid.jobby.auth.domain.validators

import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [BearerValidator::class])

@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD,
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class BearerConstraint(
        val message: String = "missing Bearer prefix",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<*>> = [],
)
