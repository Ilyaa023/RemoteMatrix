package com.remote.matrix.data.firebaseDB

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.remote.domain.interfaces.INetsBriefRepo
import com.remote.domain.models.NetBrief

class NetsBrief (): INetsBriefRepo {

    private val nets = FirebaseDatabase.getInstance(
        "https://remote-matrix-7c9ed-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(NetBrief.NETS)

    override fun getNets(callback: (ArrayList<NetBrief>) -> Unit) {
        nets.get().addOnCompleteListener {
            val listOfNets = ArrayList<NetBrief>()
            for (child in it.result.children)
                listOfNets.add(
                    NetBrief(
                        id = child.child("id").getValue(String::class.java)!!,
                        countOfBMNs = child.child("countOfBMNs")
                            .getValue(Int::class.java)!!,
                        connectedDevices = snapToArrayList(child.child("connectedDevices"))
                    ))
            Log.e("TAG", "getNets: $listOfNets")
        }
    }

    override fun setBusy(net: NetBrief, deviceName: String) {
        TODO("Not yet implemented")
    }

    override fun getNet(net: NetBrief, callback: (NetBrief) -> Unit) {
        TODO("Not yet implemented")
    }

    fun setNets(){
        nets.setValue(arrayListOf(
            NetBrief("1", 3, ArrayList()),
            NetBrief("2", 5, arrayListOf("null", "gsd")),
            NetBrief("3", 8, arrayListOf("null"))
        ))
    }

    private fun snapToArrayList(snapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        for (child in snapshot.children)
            list.add(child.getValue(String::class.java)!!)
        return list
    }
}