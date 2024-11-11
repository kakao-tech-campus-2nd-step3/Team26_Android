package org.ktc2.cokaen.wouldyouin.network.repository

import org.ktc2.cokaen.wouldyouin.network.service.ServerAPIRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CurationAPIRetrofitRepository @Inject constructor(
    private val retrofitService: ServerAPIRetrofitService
) {

}