package br.com.fiap.pureenergy

import com.google.gson.annotations.SerializedName

data class Comodo(
    @SerializedName("nome_comodo")
    val nomeComodo: String,
    @SerializedName("descricao")
    val descricao: String
)

