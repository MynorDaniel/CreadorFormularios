package com.example.creadorform.datos

class GestorPlantillas {

	private val plantillaDeclaracionesVariables = """
		number myNumber = 0
		string myText = ""
		special myQuestion = OPEN_QUESTION [
			width: 40,
			height: 12,
			label: "Pregunta abierta"
		]
	""".trimIndent()

	private val plantillaIfElse = """
		IF ( 1 >= 1 ) {
			$ instrucciones
		} ELSE IF ( 2 > 1 ) {
			$ instrucciones
		} ELSE {
			$ instrucciones
		}
	""".trimIndent()

	private val plantillaWhile = """
		WHILE ( 5 > 0 ) {
			$ instrucciones
		}
	""".trimIndent()

	private val plantillaDoWhile = """
		DO {
			$ instrucciones
		} WHILE ( 2 >= 1 )
	""".trimIndent()

	private val plantillaForClasico = """
		FOR (i = 0 ; i <= 10 ; i = i + 1 ) {
			$ instrucciones
		}
	""".trimIndent()

	private val plantillaForRango = """
		FOR (i in 1..10) {
			$ instrucciones
		}
	""".trimIndent()

	private val plantillaSeccion = """
		SECTION [
			width: 100,
			height: 60,
			pointX: 0,
			pointY: 0,
			orientation: VERTICAL,
			elements: {
				TEXT [
					content: "Titulo"
				]
			},
			styles [
				"color": #000000,
				"background color": #FFFFFF,
				"font family": SANS_SERIF,
				"text size": 14,
				"border": (1, LINE, #000000)
			]
		]
	""".trimIndent()

	private val plantillaTabla = """
		TABLE [
			width: 80,
			height: 30,
			pointX: 0,
			pointY: 0,
			elements: {
				[
					{ TEXT [ content: "Celda 1" ] },
					{ TEXT [ content: "Celda 2" ] }
				],
				[
					{ OPEN_QUESTION [ label: "Pregunta" ] },
					{ TEXT [ content: "Celda 4" ] }
				]
			},
			styles [
				"color": BLACK,
				"background color": WHITE,
				"font family": MONO,
				"text size": 12,
				"border": (1, DOTTED, BLUE)
			]
		]
	""".trimIndent()

	private val plantillaTexto = """
		TEXT [
			width: 40,
			height: 8,
			content: "Texto de ejemplo @[:star:]",
			styles [
				"color": #111111,
				"background color": #FFFFFF,
				"font family": CURSIVE,
				"text size": 14
			]
		]
	""".trimIndent()

	private val plantillaPreguntaAbierta = """
		OPEN_QUESTION [
			width: 45,
			height: 12,
			label: "Escribe tu respuesta",
			styles [
				"color": BLACK,
				"background color": WHITE,
				"font family": SANS_SERIF,
				"text size": 12
			]
		]
	""".trimIndent()

	private val plantillaPreguntaDesplegable = """
		DROP_QUESTION [
			width: 45,
			height: 12,
			label: "Selecciona una opcion",
			options: {"A", "B", "C"},
			correct: 1,
			styles [
				"color": BLACK,
				"background color": WHITE,
				"font family": SANS_SERIF,
				"text size": 12
			]
		]
	""".trimIndent()

	private val plantillaPreguntaSeleccionUnica = """
		SELECT_QUESTION [
			width: 45,
			height: 12,
			label: "Seleccion unica",
			options: {"Primera", "Segunda", "Tercera"},
			correct: 2,
			styles [
				"color": BLACK,
				"background color": WHITE,
				"font family": SANS_SERIF,
				"text size": 12
			]
		]
	""".trimIndent()

	private val plantillaPreguntaSeleccionMultiple = """
		MULTIPLE_QUESTION [
			width: 45,
			height: 12,
			label: "Seleccion multiple",
			options: {"Uno", "Dos", "Tres"},
			correct: {1, 3},
			styles [
				"color": BLACK,
				"background color": WHITE,
				"font family": SANS_SERIF,
				"text size": 12
			]
		]
	""".trimIndent()

	fun obtenerPlantillaDeclaracionesVariables(): String = plantillaDeclaracionesVariables

	fun obtenerPlantillaIfElse(): String = plantillaIfElse

	fun obtenerPlantillaWhile(): String = plantillaWhile

	fun obtenerPlantillaDoWhile(): String = plantillaDoWhile

	fun obtenerPlantillaForClasico(): String = plantillaForClasico

	fun obtenerPlantillaForRango(): String = plantillaForRango

	fun obtenerPlantillaSeccion(): String = plantillaSeccion

	fun obtenerPlantillaTabla(): String = plantillaTabla

	fun obtenerPlantillaTexto(): String = plantillaTexto

	fun obtenerPlantillaPreguntaAbierta(): String = plantillaPreguntaAbierta

	fun obtenerPlantillaPreguntaDesplegable(): String = plantillaPreguntaDesplegable

	fun obtenerPlantillaPreguntaSeleccionUnica(): String = plantillaPreguntaSeleccionUnica

	fun obtenerPlantillaPreguntaSeleccionMultiple(): String = plantillaPreguntaSeleccionMultiple

}