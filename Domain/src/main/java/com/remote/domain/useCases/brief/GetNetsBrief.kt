package com.remote.domain.useCases.brief

import com.remote.domain.interfaces.INetsBriefRepo
import com.remote.domain.models.NetBrief

class GetNetsBrief (private val repo: INetsBriefRepo) {
    fun execute (callback: (ArrayList<NetBrief>) -> Unit){
        repo.getNets { callback(it) }
    }
}