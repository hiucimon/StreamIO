package org.hiucimon.utility

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import kotlin.coroutines.experimental.buildSequence

val asciiLineReader = buildSequence {
    val fn="/Users/ndb338/StreamingReader/resources/emb.cpy"
    val f= File(fn)
    val input= FileReader(f)
    val reader = BufferedReader(input)
    try {
        while (true) {
            val line = reader.readLine()
            if (line == null) break
            yield(line)
        }
    } finally {
        reader.close()
    }
    yield(null)
}

val sequence = buildSequence {
    val start = 0
    // yielding a single value
    yield(start)
    // yielding an iterable
    yieldAll(1..5 step 2)
    // yielding an infinite sequence
    yieldAll(generateSequence(8) { it * 3 })
}


// https://www.ibm.com/support/knowledgecenter/zosbasics/com.ibm.zos.zconcepts/zconcepts_159.htm

enum class FileType {ASCII,F,FB,V,VB}
data class DataFile(val filename:String,val filetype:FileType=FileType.ASCII,val lrecl:Int=0,val blksize:Int=32768) {
    val f=File(filename)
    init {

    }
    val read = buildSequence {
        when (filetype) {
            FileType.ASCII  ->  {
                val input=FileReader(f)
                val reader = BufferedReader(input)
                try {
                    while (true) {
                        val line = reader.readLine()
                        if (line == null) break
                        yield(line)
                    }
                } finally {
                    reader.close()
                }
            }
            FileType.F      ->  {
                val ReadBuffer=ByteArray(lrecl)
                val inStream= FileInputStream(filename)
                while (true) {
                    val bytesRead=inStream.read(ReadBuffer)
                    if (bytesRead==-1) break
//                    println("I read ${bytesRead}")

                    val res=ReadBuffer.toList()
                    val res2=arrayListOf<Int>()
                    res.forEach { res2.add(it.Value()) }
                    yield(res2)
                }

            }
            FileType.FB     ->  {

            }
            FileType.V      ->  {
                val HEADER=ByteArray(4)

                val inStream= FileInputStream(filename)
                while (true) {
                    val headerRead=inStream.read(HEADER)
                    if (headerRead<4) break
                    val hdrres=HEADER.toList()
                    val res2=arrayListOf<Int>()
                    hdrres.forEach { res2.add(it.Value()) }
//                    LOG ERROR FOR ALL EXCEPT -1
                    val LRECL=HEADER[0].Value() *256 +HEADER[1].Value() -4
                    val ReadBuffer=ByteArray(LRECL)
                    val bytesRead=inStream.read(ReadBuffer)
                    if (bytesRead<LRECL) break
//                    LOG ERROR FOR ALL EXCEPT -1

                    val res=ReadBuffer.toList()
//                    res2.addAll(res)
                    res.forEach { res2.add(it.Value()) }
                    yield(res2)
                }

            }
            FileType.VB     ->  {

            }
            else            ->  {

            }
        }
    }
}


//fun main(args : Array<String>) {
//    val fn1="/Users/ndb338/StreamingReader/resources/stag_test_file.v.ebcdic"
//    val fn2="/Users/ndb338/FOP/fontsafp.afp"
//    val b=DataFile(fn1,FileType.F,256)
//    var offset=0
//    b.read.forEach {
//        println("Next Record")
//        offset=Dump(it as ArrayList<Int>,start=offset)
//    }
//}
//
//
//fun main(args : Array<String>) {
////    val a=Hex.XFF
//    print("hello ${Xff}")
////    genHexConst()
//}
//
//
//fun main(args : Array<String>) {
////    println(sequence.take(7).toList()) // [0, 1, 3, 5, 8, 24, 72]
//    val fn1="/Users/ndb338/StreamingReader/resources/stag_test_file.v.ebcdic"
////    val a=DataFile("/Users/ndb338/StreamingReader/resources/emb.cpy")
////    a.read.forEach { println(it) }
//    val b=DataFile(fn1,FileType.V)
//    var offset=0
//    var record=0
//    b.read.forEach {
//        record+=1
//        println("Record ${record}")
////        println(it)
//        offset=Dump(it as ArrayList<Int>,start=0)
//        println()
//    }
////    println(asciiLineReader.take(4).toList())
////    asciiLineReader.forEach { println(it) }
//}
//
