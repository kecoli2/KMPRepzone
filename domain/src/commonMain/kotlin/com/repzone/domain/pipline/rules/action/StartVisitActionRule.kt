package com.repzone.domain.pipline.rules.action

import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType

class StartVisitActionRule(
    override val id: String = "start_visit",
    override val title: String = "Ziyaret Başlat",
) :  Rule {
    //region Field
    override val type: RuleType = RuleType.ACTION
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun execute(context: PipelineContext): RuleResult {

        //database e kayıt et ve visit id al
        val visitId = "visit"
        context.putData("visit_id", visitId)

        return RuleResult.Success(this)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}