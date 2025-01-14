package com.example.util.simpletimetracker

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpletimetracker.utils.BaseUiTest
import com.example.util.simpletimetracker.utils.NavUtils
import com.example.util.simpletimetracker.utils.checkViewDoesNotExist
import com.example.util.simpletimetracker.utils.checkViewIsDisplayed
import com.example.util.simpletimetracker.utils.clickOnViewWithId
import com.example.util.simpletimetracker.utils.clickOnViewWithText
import com.example.util.simpletimetracker.utils.longClickOnView
import com.example.util.simpletimetracker.utils.tryAction
import com.example.util.simpletimetracker.utils.withCardColor
import com.example.util.simpletimetracker.utils.withTag
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DeleteRecordTypeTest : BaseUiTest() {

    @Test
    fun deleteRecordType() {
        val name = "Test"
        val color = firstColor
        val icon = firstIcon

        // Add item
        testUtils.addActivity(name = name, color = color, icon = icon)
        testUtils.addRecord(name)

        tryAction {
            checkViewIsDisplayed(
                allOf(
                    withId(R.id.viewRecordTypeItem),
                    hasDescendant(withText(name)),
                    hasDescendant(withTag(icon)),
                    withCardColor(color)
                )
            )
        }

        // Archive item
        longClickOnView(withText(name))
        checkViewIsDisplayed(withId(R.id.btnChangeRecordTypeDelete))
        clickOnViewWithId(R.id.btnChangeRecordTypeDelete)

        // TODO check message

        // Record type is deleted
        checkViewDoesNotExist(
            allOf(
                withId(R.id.viewRecordTypeItem),
                hasDescendant(withText(name)),
                hasDescendant(withTag(icon)),
                withCardColor(color)
            )
        )

        // Delete
        NavUtils.openSettingsScreen()
        NavUtils.openArchiveScreen()
        clickOnViewWithText(name)
        clickOnViewWithText(R.string.archive_dialog_delete)
        clickOnViewWithText(R.string.archive_dialog_delete)
        checkViewDoesNotExist(withText(name))
        checkViewIsDisplayed(withText(R.string.archive_empty))
        pressBack()

        // Record removed
        NavUtils.openRecordsScreen()
        checkViewDoesNotExist(withText(name))
    }
}
