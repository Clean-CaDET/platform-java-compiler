import java.io.File

const val root = "test\\"
const val javaFileExtension = "java"

// TODO Crashed on primitive array syntax (array[0].call())
// TODO Crashed on enum parsing when it has functions (FieldNamingPolicy.java in gson project)
// TODO Crashed on interface method calls when doing DI (parent)
// TODO CadetClass serialization issue due to roundabout references Member <-> Class
// TODO Working with enums is unsupported, arguments of that type will be regarded as Wildcards
// TODO Global calls are not registered as usages nor are resolved
//      private Type field = SomeReference.callMethod()

// Progres
// Ceo program moze da radi sa jednim JavaCodeParserom, sve je thread-safe
// Dodati modifieri u CadetMembera
// Popravljeno sve sto nije radilo za parsiranje ili je bilo //TODO-ovano

// Sta ne radi
// Enumeracije
// Interfejsi
// Prazni konstruktori kada nisu eksplicitno definisani (implicit empty constr.)
// Pristup primitivnim arrayevima -> array[0]
// Globalni pozivi/pristupi bilo cemu u okviru klase, npr kod inic. polja
// Problemi sa serijalizacijom CadetClass objekta zbog roundabout reference sa Memberom poljima itd

// QA
// Kako da podacima predstavimo threaded/not-threaded razliku  -  pre kraj eksperimentalni rezultati
// Template za pisanje diplomskog
// Da li da komuniciramo sa glavnom platformom preko konzolnog ispisa ili preko HTTP kanala - svj
// Ponoviti spisak stvari koje treba implementirati za analizu koda

// Metrike:
// Broj stejtmenta u funkciji - effective lines of code
// Broj parametara
// Broj lok. prom.

// Broj metoda u klasi
// Broj polja u klasi
// LCOM metrika za klasu - kohezija


fun main(args: Array<String>) {

    parse("gson")
    parse("json_iter")
    parse("tests")
    parse("shapes")
}

private fun parse(directory: String) {
    JavaCodeParser().parseSourceCode(extractSourceCode(getAllFilePaths(root + directory)))
}

private fun getAllFilePaths(path: String): List<String> {
    return mutableListOf<String>()
        .also { paths ->
            File(path).walkBottomUp().forEach {
                if (it.isFile && it.extension == javaFileExtension)
                    paths.add(it.absolutePath)
            }
        }
}

private fun extractSourceCode(paths: List<String>): List<String> {
    return mutableListOf<String>()
        .also { sourceCodeList ->
            paths.forEach { filePath ->
                sourceCodeList.add(File(filePath).readText())
            }
        }
}
