package com.palich.fragments

import com.google.gson.annotations.SerializedName


data class SuperHero(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("powerstats") var powerstats: SuperHeroPowerStats? = SuperHeroPowerStats(),
    @SerializedName("appearance") var appearance: SuperHeroAppearance? = SuperHeroAppearance(),
    @SerializedName("biography") var biography: SuperHeroBiography? = SuperHeroBiography(),
    @SerializedName("work") var work: SuperHeroWork? = SuperHeroWork(),
    @SerializedName("connections") var connections: SuperHeroConnections? = SuperHeroConnections(),
    @SerializedName("images") var images: SuperHeroImages? = SuperHeroImages()
)

data class SuperHeroPowerStats(
    @SerializedName("intelligence") var intelligence: Int? = null,
    @SerializedName("strength") var strength: Int? = null,
    @SerializedName("speed") var speed: Int? = null,
    @SerializedName("durability") var durability: Int? = null,
    @SerializedName("power") var power: Int? = null,
    @SerializedName("combat") var combat: Int? = null
)

data class SuperHeroAppearance(
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("race") var race: String? = null,
    @SerializedName("height") var height: ArrayList<String> = arrayListOf(),
    @SerializedName("weight") var weight: ArrayList<String> = arrayListOf(),
    @SerializedName("eyeColor") var eyeColor: String? = null,
    @SerializedName("hairColor") var hairColor: String? = null
)

data class SuperHeroBiography(
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("alterEgos") var alterEgos: String? = null,
    @SerializedName("aliases") var aliases: ArrayList<String> = arrayListOf(),
    @SerializedName("placeOfBirth") var placeOfBirth: String? = null,
    @SerializedName("firstAppearance") var firstAppearance: String? = null,
    @SerializedName("publisher") var publisher: String? = null,
    @SerializedName("alignment") var alignment: String? = null
)

data class SuperHeroWork(
    @SerializedName("occupation") var occupation: String? = null,
    @SerializedName("base") var base: String? = null
)

data class SuperHeroConnections(
    @SerializedName("groupAffiliation") var groupAffiliation: String? = null,
    @SerializedName("relatives") var relatives: String? = null
)

data class SuperHeroImages(
    @SerializedName("xs") var xs: String? = null,
    @SerializedName("sm") var sm: String? = null,
    @SerializedName("md") var md: String? = null,
    @SerializedName("lg") var lg: String? = null
)