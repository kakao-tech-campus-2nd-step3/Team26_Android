package org.ktc2.cokaen.wouldyouin.network.Repository

import org.ktc2.cokaen.wouldyouin.network.Service.ServerAPIRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CurationAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ServerAPIRetrofitService
) {

}