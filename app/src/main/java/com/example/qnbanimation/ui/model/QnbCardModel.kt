package com.example.qnbanimation.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.qnbanimation.R

data class CardOperationUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val amountFormatted: String,
    val isCredit: Boolean = false
)

data class QnbCard3DModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val cardNumberMasked: String,
    val cardHolder: String,
    val badgeLabel: String,
    val balanceFormatted: String,
    val spentFormatted: String,
    val totalLimitFormatted: String,
    val remainingFormatted: String,
    val progress: Float,
    val primaryGradient: List<Color>,
    val accentColor: Color,
    val operations: List<CardOperationUiModel>,
    @param:DrawableRes val backgroundDrawableRes: Int = R.drawable.background_card
)

enum class QuickActionIconType { DETAILS, PIN, FREEZE, SETTINGS }

enum class BottomNavTab { HOME, TRANSFERS, CARDS, MORE }

val Sample3DCards = listOf(
    QnbCard3DModel(
        id = "1",
        title = "Visa Platinum",
        subtitle = "Debit Card •••• 7745",
        cardNumberMasked = "•••• 7745",
        cardHolder = "TARIK BENALI",
        badgeLabel = "QATAR AIRWAYS",
        balanceFormatted = "MAD 148,920.00",
        spentFormatted = "2,178.00 MAD",
        totalLimitFormatted = "10,000.00 MAD",
        remainingFormatted = "7,822.00 MAD",
        progress = 0.218f,
        primaryGradient = listOf(Color(0xFF0B2161), Color(0xFF040E30), Color(0xFF02071B)),
        accentColor = Color(0xFFD4AF37),
        operations = listOf(
            CardOperationUiModel("op1", "Morocco Mall Boutique", "Today • 18:42", "- MAD 1,250.00"),
            CardOperationUiModel("op2", "Client Wire Settlement", "Yesterday", "+ MAD 14,000.00", isCredit = true),
            CardOperationUiModel("op3", "ONCF Rail Pass", "Nov 22", "- MAD 340.00")
        ),
        backgroundDrawableRes = R.drawable.background_card
    ),
    QnbCard3DModel(
        id = "2",
        title = "First Plus Card",
        subtitle = "Credit Card •••• 9104",
        cardNumberMasked = "•••• 9104",
        cardHolder = "TARIK BENALI",
        badgeLabel = "FIRST PLUS",
        balanceFormatted = "MAD 24,510.50",
        spentFormatted = "4,500.00 MAD",
        totalLimitFormatted = "25,000.00 MAD",
        remainingFormatted = "20,500.00 MAD",
        progress = 0.18f,
        primaryGradient = listOf(Color(0xFF5A0D28), Color(0xFF2E0413), Color(0xFF150209)),
        accentColor = Color(0xFFF5D77F),
        operations = listOf(
            CardOperationUiModel("op4", "Marjane Hypermarket", "Today • 12:15", "- MAD 618.40"),
            CardOperationUiModel("op5", "ATM Cash Withdrawal", "Nov 23", "- MAD 1,000.00")
        ),
        backgroundDrawableRes = R.drawable.background_card_1
    ),
    QnbCard3DModel(
        id = "3",
        title = "World Elite Titanium",
        subtitle = "Prepaid Card •••• 3371",
        cardNumberMasked = "•••• 3371",
        cardHolder = "TARIK BENALI",
        badgeLabel = "WORLD ELITE",
        balanceFormatted = "EUR 1,850.00",
        spentFormatted = "420.00 EUR",
        totalLimitFormatted = "2,000.00 EUR",
        remainingFormatted = "1,580.00 EUR",
        progress = 0.21f,
        primaryGradient = listOf(Color(0xFF1E222A), Color(0xFF11141A), Color(0xFF08090C)),
        accentColor = Color(0xFF86E3CE),
        operations = listOf(
            CardOperationUiModel("op6", "Air France Booking", "Nov 20", "- EUR 420.00"),
            CardOperationUiModel("op7", "Wallet Top-Up", "Nov 19", "+ EUR 1,500.00", isCredit = true)
        ),
        backgroundDrawableRes = R.drawable.background_card
    )
)
