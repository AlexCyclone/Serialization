package com.devianta;

import java.lang.annotation.*;

@Inherited
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Save {
}
