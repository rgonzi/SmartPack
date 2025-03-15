package com.inovatech.smartpack.ui.utils


fun String.isValidEmail(): Boolean {
    //El Regex comprova que el correu tingui lletres-numeros abans de la @,
    // després i acabi amb . i lletres
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(this)

}

fun String.isValidPassword(): Boolean {
    //El Regex ha de complir amb 8 caràcters, 1 majúscula i 1 numèric
    val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    return passwordRegex.matches(this)
}