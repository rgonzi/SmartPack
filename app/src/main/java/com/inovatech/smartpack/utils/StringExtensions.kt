package com.inovatech.smartpack.utils

/**
 * Funció d'extensió de String que permet comprovar si un correu electrònic és correcte
 * en funció d'un Regex definit
 */
fun String.isValidEmail(): Boolean {
    //El Regex comprova que el correu tingui lletres-numeros abans de la @,
    // després i acabi amb . i lletres
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(this)

}

/**
 * Funció d'extensió de String que permet comprovar si una contrasenya és correcte en funció
 * d'un Regex definit. En aquest cas els requeriments són: 8 caràcters mínim, ha d'incloure
 * almenys 1 majúscula i també almenys 1 nombre.
 */
fun String.isValidPassword(): Boolean {
    //El Regex ha de complir amb 8 caràcters, 1 majúscula i 1 numèric
    val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    return passwordRegex.matches(this)
}