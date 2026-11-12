package dmirzayants.voidvpn.ui.theme

import androidx.compose.ui.graphics.Color

/** Фон под OLED. */
val VoidBlack = Color(0xFF000000)

/** Цвета текста на #000000 (из Void VPN Kit, раздел 03). */
object VoidText {
    val primary = Color(0xFFF4F4F5)     // 19.1:1
    val secondary = Color(0xFF9A9AA2)   // 7.5:1
    val tertiary = Color(0xFF7A7A82)    // 4.9:1
    val disabled = Color(0xFF55555C)    // 2.9:1 — только неактивное
    val onLight = Color(0xFF000000)     // текст на кнопке #F4F4F5
}

/** Цвет = состояние. Бренд монохромный. */
object VoidStatus {
    val protected = Color(0xFF3DF5A0)   // мятный — защищено
    val connecting = Color(0xFFFFC34D)  // жёлтый — подключение / внимание
    val error = Color(0xFFFF4D5E)       // красный — проблема
}

/** Цвет действия (основная кнопка). */
val VoidAction = Color(0xFFF4F4F5)

/** Поверхности (из экранов макета: карточки, поля, кнопки). */
object VoidSurface {
    val base = Color(0xFF0E0E10)
    val card = Color(0xFF141416)
    val cardAlt = Color(0xFF17171A)
    val raised = Color(0xFF1E1E22)      // карточка диалога, поля ввода
    val sheet = Color(0xFF121214)       // нижняя шторка (ошибки)
}

/** Обводки и разделители. */
object VoidOutline {
    val subtle = Color(0xFF1C1C20)      // разделители, внешнее кольцо
    val default = Color(0xFF2A2A2E)     // неактивный индикатор, внутреннее кольцо
    val strong = Color(0xFF3A3A40)      // радио, обводка полей
    val handle = Color(0xFF4A4A50)      // индикатор жеста
}

/** Дополнительный текст из экранов (пункты списков). */
val VoidTextSoft = Color(0xFFC9C9CF)
