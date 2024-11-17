package org.ktc2.cokaen.wouldyouin.network.repository

import android.util.Log
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyCurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ApiResponseBodyCurationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import org.ktc2.cokaen.wouldyouin.network.service.CurationAPIRetrofitService
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CurationAPIRetrofitRepository @Inject constructor(
    private val curationRetrofitService: CurationAPIRetrofitService
) {
    suspend fun createCuration(request: CurationCreateRequest): CurationResponse {
        try {
            val response = curationRetrofitService.createCuration(request)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "큐레이션 생성에 실패했습니다"
                            )
                        }
                    }
                        ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }

                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("CreateCuration", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun updateCuration(request: CurationEditRequest): CurationResponse {
        try {
            val response = curationRetrofitService.updateCuration(request)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "큐레이션 생성에 실패했습니다"
                            )
                        }
                    }
                        ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }

                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("CreateCuration", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun getCurationList(
        area: String = "전체",
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): CurationSliceResponse {
        try {
            val response = curationRetrofitService.getCurationList(area, page, size, lastId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "큐레이션 목록 조회에 실패했습니다"
                            )
                        }
                    }
                        ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }

                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("GetCurationList", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun getCurationDetail(curationId: Long): CurationResponse {
        try {
            val response = curationRetrofitService.getCurationDetail(curationId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "큐레이션 상세 정보 조회에 실패했습니다"
                            )
                        }
                    } ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("GetCurationDetail", "Fetch failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun getCurationsByCurator(
        curationId: Long,
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): CurationSliceResponse {
        try {
            val response = curationRetrofitService.getCurationsByCurator(curationId, page, size, lastId)
            return when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        if (body.success) {
                            Log.d("CuratorCurations", "${body.data}")
                            body.data
                        } else {
                            throw ServerCommonAPIRetrofitRepository.CustomException(
                                body.message ?: "큐레이션 목록 조회에 실패했습니다"
                            )
                        }
                    }
                        ?: throw ServerCommonAPIRetrofitRepository.CustomException("서버로부터 유효한 응답을 받지 못했습니다")
                }

                else -> {
                    val errorBody = response.errorBody()?.string()
                    throw ServerCommonAPIRetrofitRepository.CustomException("서버 응답 오류: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("CurationsByCurator", "Create failed", e)
            throw when (e) {
                is IOException -> ServerCommonAPIRetrofitRepository.CustomException("네트워크 연결을 확인해주세요")
                is HttpException -> ServerCommonAPIRetrofitRepository.CustomException("서버 통신 오류: ${e.code()}")
                else -> e
            }
        }
    }

    suspend fun deleteCuration(curationId: Long): Boolean {
        return try {
            val response = curationRetrofitService.deleteCuration(curationId)
            if (response.isSuccessful) {
                true
            } else {
                throw Exception("큐레이션 삭제에 실패했습니다. 오류 코드: ${response.code()}")
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> throw Exception("네트워크 연결을 확인해주세요.")
                is HttpException -> throw Exception("서버 통신 중 오류가 발생했습니다.")
                else -> throw e
            }
        }
    }
}
