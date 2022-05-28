package com.akj.nulloverflow

//리사이클러 뷰에서 사용하는 데이터클래스, 해당 클래스에 선언된 변수들이 리사이클러뷰(MainOption화면에 있는 테라스 자리 나타내는 뷰)에 저장됨
//사용중인지 아닌지에 대한 값은 서버 및 DB작업이 완료되면 추가할 예정
data class Info(var floor: Int, var point: String, var mac: String, var uses: Boolean, var pur: String?)