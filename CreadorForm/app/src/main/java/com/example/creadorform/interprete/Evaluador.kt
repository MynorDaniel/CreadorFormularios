package com.example.creadorform.interprete

import com.example.creadorform.dominio.*
import com.example.creadorform.interprete.ast.*
import com.example.creadorform.interprete.analizadorsemantico.TablaDeSimbolos
import com.example.creadorform.interprete.analizadorsemantico.Tipo
import kotlin.math.pow
import com.example.creadorform.datos.PokeAPI

class Evaluador : Visitor<Any> {

    private var formularioActual: Formulario? = null

    data class AtributoEvaluado(val nombre: String, val valor: Any?)

    private val specials = mutableMapOf<String, NodoPregunta>()
    private val specialAridad = mutableMapOf<String, Int>()
    private var comodinesActivos: List<Any>? = null
    private var indiceComodinActivo: Int = 0

    private var tablaDeSimbolos: TablaDeSimbolos = TablaDeSimbolos()

    private val valores = mutableMapOf<String, Any?>()

    private var presupuestoIteraciones: Long = 200_000

    fun setMaxIteraciones(max: Long) {
        presupuestoIteraciones = max.coerceAtLeast(0)
    }

    private fun consumirIteracion(contexto: String) {
        if (presupuestoIteraciones <= 0) {
            throw RuntimeException(
                "Posible bucle infinito detectado ($contexto): se excedió el máximo de iteraciones permitido"
            )
        }
        presupuestoIteraciones--
    }

    fun evaluar(nodo: NodoPrograma): Formulario {
        tablaDeSimbolos.declarar("NUMBER", Tipo.VOID)
        return nodo.aceptar(this) as Formulario
    }

    override fun visit(nodo: NodoPrograma?): Formulario {
        val formulario = Formulario()
        formularioActual = formulario

        nodo?.sentencias?.forEach { sentenciaAst ->
            val sentencia = sentenciaAst.aceptar(this)
            if (sentencia is Elemento) {
                formulario.addElemento(sentencia)
            }
        }

        nodo?.elementos?.forEach { elementoAst ->
            val elemento = elementoAst.aceptar(this)
            if (elemento is Elemento) {
                formulario.addElemento(elemento)
            }
        }

        return formulario
    }

    override fun visit(nodo: NodoBloque?): Any? {
        if (nodo == null) return null
        nodo.sentencias?.forEach { sentenciaAst ->
            val r = sentenciaAst.aceptar(this)
            if (r is Pregunta) {
                formularioActual?.addElemento(r)
            }
        }
        return null
    }


    override fun visit(nodo: NodoListaElementos?): Any? {
        if (nodo == null) return emptyList<Elemento>()
        val res = mutableListOf<Elemento>()
        val elems = nodo.elementos ?: emptyList()
        for (e in elems) {
            val ev = e.aceptar(this)
            if (ev is Elemento) res.add(ev)
        }
        return res
    }

    override fun visit(nodo: NodoListaExpresiones?): Any? {
        if (nodo == null) return emptyList<Any?>()

        val exprs = nodo.expresiones ?: emptyList()
        if (exprs.isEmpty()) return emptyList<Any?>()

        val first = exprs.first()
        if (first is NodoLiteral && first.tipoLiteral.equals("ri", ignoreCase = true)) {

            val inicio = parseInt(first.aceptar(this), 1)

            val second = exprs.getOrNull(1)
                ?: throw RuntimeException("Rango de who_is_that_pokemon requiere dos argumentos")

            val finValor = second.aceptar(this)
            val fin = parseInt(finValor, inicio)

            val nombres = PokeAPI().obtenerNombresPokemon(inicio, fin)

            return nombres.toList()
        }

        return exprs.map { it.aceptar(this) }
    }


    override fun visit(nodo: NodoFilasTabla?): Any? {
        if (nodo == null) return emptyList<List<Elemento>>()
        val filas = nodo.filas ?: emptyList()
        return filas.map { fila ->
            fila.mapNotNull { celda -> celda.aceptar(this) as? Elemento }
        }
    }

    override fun visit(nodo: NodoEstilos?): Any? {
        if (nodo == null) return Estilos()
        val estilos = Estilos()

        val entradas = nodo.estilos ?: emptyList()
        for (entrada in entradas) {
            val ev = entrada.aceptar(this)
            when (ev) {
                is Borde -> estilos.setBorde(ev)
                is Pair<*, *> -> {
                    val clave = (ev.first as? String)?.trim()?.lowercase() ?: continue
                    val valor = ev.second
                    when (clave) {
                        "color" -> estilos.setColor(parseColor(valor))
                        "background color", "background-color", "background" -> estilos.setColorFondo(parseColor(valor))
                        "font family", "font-family", "font" -> estilos.setFuente(parseFuente(valor))
                        "text size", "text-size", "textsize" -> estilos.setSizeTexto(parseInt(valor, 14))
                        "border", "borde" -> if (valor is Borde) estilos.setBorde(valor)
                    }
                }
            }
        }
        return estilos
    }

    override fun visit(nodo: NodoEstilo?): Any? {
        if (nodo == null) return null
        val clave = nodo.clave
        val valor = nodo.valor?.aceptar(this)
        return Pair(clave, valor)
    }

    override fun visit(nodo: NodoAtributo?): Any? {
        if (nodo == null) return null
        val nombre = nodo.nombre
        val valor = when {
            (nombre.equals("opciones", ignoreCase = true) || nombre.equals("options", ignoreCase = true))
                && nodo.valor is NodoCadenaCompuesta -> {
                val cc = nodo.valor as NodoCadenaCompuesta
                cc.partes.map { it.aceptar(this)?.toString() ?: "" }
            }

            (nombre.equals("respuestas", ignoreCase = true) || nombre.equals("correct", ignoreCase = true))
                && nodo.valor is NodoLiteral
                && (nodo.valor as NodoLiteral).tipoLiteral.equals("lista_int", ignoreCase = true) -> {
                parseListaInts((nodo.valor as NodoLiteral).valor)
            }

            else -> nodo.valor?.aceptar(this)
        }
        return AtributoEvaluado(nombre, valor)
    }

    override fun visit(nodo: NodoBorde?): Any? {
        if (nodo == null) return null

        val grosor = parseInt(nodo.num?.aceptar(this), 0)
        val tipoRaw = nodo.tipo?.aceptar(this)
        val colorRaw = nodo.color?.aceptar(this)

        val tipo = when ((tipoRaw ?: "LINE").toString().trim().uppercase()) {
            "DOTTED" -> TipoBorde.DOTTED
            "DOUBLE" -> TipoBorde.DOUBLE
            else -> TipoBorde.LINE
        }

        return Borde(grosor, tipo, parseColor(colorRaw))
    }

    override fun visit(nodo: NodoSeccion?): Any? {
        if (nodo == null) return null
        val seccion = Seccion()

        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(seccion, attrs)

        // elementos
        val children = attr(attrs, "elements", "contenido")
        if (children is List<*>) {
            children.filterIsInstance<Elemento>().forEach { seccion.addElemento(it) }
        }
        return seccion
    }

    override fun visit(nodo: NodoTexto?): Any? {
        if (nodo == null) return null
        val texto = Texto()
        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(texto, attrs)
        attr(attrs, "content", "contenido")?.let { texto.setContenido(it.toString()) }
        return texto
    }

    override fun visit(nodo: NodoEmoji?): String {
        return when (nodo?.tipo) {
            "risa" -> "\uD83D\uDE00"      // 😀
            "triste" -> "\uD83D\uDE22"    // 😢
            "serio" -> "\uD83D\uDE10"     // 😐
            "corazon" -> "\u2764\uFE0F"   // ❤️
            "estrella" -> "\u2B50"        // ⭐
            "gato" -> "\uD83D\uDC31"      // 🐱
            else -> ""
        }
    }

    override fun visit(nodo: NodoTabla?): Any? {
        if (nodo == null) return null
        val tabla = Tabla()
        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(tabla, attrs)

        val filas = attr(attrs, "elements", "filas")
        if (filas is List<*> && filas.isNotEmpty() && filas[0] is List<*>) {
            val lineas: MutableList<Linea> = filas
                .filterIsInstance<List<*>>()
                .map { fila ->
                    val elementos = fila.filterIsInstance<Elemento>().toMutableList()
                    Linea(elementos)
                }
                .toMutableList()
            tabla.setLineas(lineas)
        }

        return tabla
    }

    override fun visit(nodo: NodoPregunta?): Any? {
        return null
    }

    override fun visit(nodo: NodoPreguntaAbierta?): Any? {
        if (nodo == null) return null
        val pregunta = PreguntaAbierta()
        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(pregunta, attrs)
        attr(attrs, "label", "contenido", "content")?.let { pregunta.setLabel(it.toString()) }
        return pregunta
    }

    override fun visit(nodo: NodoPreguntaSeleccionUnica?): Any? {
        if (nodo == null) return null
        val pregunta = PreguntaUnica()
        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(pregunta, attrs)
        attr(attrs, "label", "contenido", "content")?.let { pregunta.setLabel(it.toString()) }
        attr(attrs, "options", "opciones")?.let { opts ->
            val lista = toStringList(opts).toMutableList()
            pregunta.setOpciones(lista)
        }
        attr(attrs, "correct", "respuesta")?.let { pregunta.setOpcionCorrecta(parseInt(it, -1)) }
        return pregunta
    }

    override fun visit(nodo: NodoPreguntaSeleccionMultiple?): Any? {
        if (nodo == null) return null
        val pregunta = PreguntaMultiple()
        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(pregunta, attrs)
        attr(attrs, "label", "contenido", "content")?.let { pregunta.setLabel(it.toString()) }
        attr(attrs, "options", "opciones")?.let { opts ->
            val lista = toStringList(opts).toMutableList()
            pregunta.setOpciones(lista)
        }
        attr(attrs, "correct", "respuestas")?.let { corr ->
            val indices = toIntList(corr).toMutableList()
            pregunta.setOpcionesCorrectas(indices)
        }
        return pregunta
    }

    override fun visit(nodo: NodoPreguntaDesplegable?): Any? {
        if (nodo == null) return null
        val pregunta = PreguntaDesplegable()
        val attrs = recolectarAtributos(nodo.atributos)
        aplicarAtributosElemento(pregunta, attrs)
        attr(attrs, "label", "contenido", "content")?.let { pregunta.setLabel(it.toString()) }
        attr(attrs, "options", "opciones")?.let { opts ->
            val lista = toStringList(opts).toMutableList()
            pregunta.setOpciones(lista)
        }
        attr(attrs, "correct", "respuesta")?.let { pregunta.setOpcionCorrecta(parseInt(it, -1)) }
        return pregunta
    }

    override fun visit(nodo: NodoDeclaracionVariable?): Unit {
        if (nodo == null) return

        val tipoDecl = try {
            Tipo.valueOf(nodo.tipo.uppercase())
        } catch (_: IllegalArgumentException) {
            Tipo.ERROR
        }

        val id = nodo.identificador
        val ok = tablaDeSimbolos.declarar(id, tipoDecl)
        if (!ok) {
            throw RuntimeException("Variable ya declarada: $id")
        }

        val valorInit = nodo.expresionInicial?.aceptar(this)
        if (valorInit != null) {
            val tipoValor = inferirTipo(valorInit)
            if (tipoDecl != Tipo.ERROR && tipoValor != Tipo.ERROR && tipoValor != tipoDecl) {
                throw RuntimeException("Tipo incompatible en declaración de $id")
            }
        }

        valores[id] = valorInit
        return
    }

    override fun visit(nodo: NodoDeclaracionSpecial?): Unit {
        if (nodo == null) return
        val id = nodo.identificador
        val plantilla = nodo.valor
        if (plantilla != null) {
            specials[id] = plantilla
            specialAridad[id] = contarComodines(plantilla)
        }
        return
    }

    override fun visit(nodo: NodoAsignacion?): Any? {
        if (nodo == null) return null

        val id = nodo.identificador
        val tipoVar = tablaDeSimbolos.buscar(id)
            ?: throw RuntimeException("Variable no declarada: $id")

        val valor = nodo.expresion?.aceptar(this)
        val tipoValor = if (valor == null) Tipo.VOID else inferirTipo(valor)

        println(id + tipoVar)
        println(valor.toString() + tipoValor)

        if (tipoValor != Tipo.ERROR && tipoValor != tipoVar) {
            throw RuntimeException("Tipo incompatible en asignación a $id")
        }

        valores[id] = valor
        return null
    }

    override fun visit(nodo: NodoSentenciaElemento?): Any? {
        if (nodo == null) return null
        val elemento = nodo.elemento?.aceptar(this)
        return elemento
    }

    override fun visit(nodo: NodoIf?): Any? {
        if (nodo == null) return null
        val cond = nodo.condicion?.aceptar(this)
        if (verdadero(cond)) {
            nodo.bloqueThen?.aceptar(this)
        } else {
            nodo.cola?.aceptar(this)
        }
        return null
    }

    override fun visit(nodo: NodoElseIf?): Any? {
        if (nodo == null) return null
        val cond = nodo.condicion?.aceptar(this)
        if (verdadero(cond)) {
            nodo.bloque?.aceptar(this)
        } else {
            nodo.siguiente?.aceptar(this)
        }
        return null
    }

    override fun visit(nodo: NodoElse?): Any? {
        if (nodo == null) return null
        nodo.bloque?.aceptar(this)
        return null
    }

    override fun visit(nodo: NodoFinIf?): Any? {
        return null
    }

    override fun visit(nodo: NodoWhile?): Any? {
        if (nodo == null) return null
        while (verdadero(nodo.condicion?.aceptar(this))) {
            consumirIteracion("while")
            nodo.bloque?.aceptar(this)
        }
        return null
    }

    override fun visit(nodo: NodoDoWhile?): Any? {
        if (nodo == null) return null
        do {
            consumirIteracion("do-while")
            nodo.bloque?.aceptar(this)
        } while (verdadero(nodo.condicion?.aceptar(this)))
        return null
    }

    override fun visit(nodo: NodoForClasico?): Any? {
        if (nodo == null) return null
        val id = nodo.inicializacion.identificador
        if (tablaDeSimbolos.buscar(id) == null) {
            tablaDeSimbolos.declarar(id, Tipo.NUMBER)
        }
        nodo.inicializacion?.aceptar(this)
        while (verdadero(nodo.condicion?.aceptar(this))) {
            consumirIteracion("for-clasico")
            nodo.bloque?.aceptar(this)
            nodo.actualizacion?.aceptar(this)
        }
        return null
    }

    override fun visit(nodo: NodoForRango?): Any? {
        if (nodo == null) return null
        val id = nodo.identificador

        val inicio = parseInt(nodo.inicio?.aceptar(this), 0)
        val fin = parseInt(nodo.fin?.aceptar(this), 0)

        if (tablaDeSimbolos.buscar(id) == null) {
            tablaDeSimbolos.declarar(id, Tipo.NUMBER)
        }
        for (i in inicio..fin) {
            consumirIteracion("for-rango")
            valores[id] = i
            nodo.bloque?.aceptar(this)
        }
        return null
    }

    override fun visit(nodo: NodoExpresionBinaria?): Any? {
        if (nodo == null) return null

        val op = nodo.operador
        val izq = nodo.izquierda?.aceptar(this)
        val der = nodo.derecha?.aceptar(this)

        fun asDouble(v: Any?): Double? = when (v) {
            is Int -> v.toDouble()
            is Long -> v.toDouble()
            is Float -> v.toDouble()
            is Double -> v
            is String -> v.toDouble()
            else -> null
        }

        fun verdadero(v: Any?): Boolean {
            return when (v) {
                is Boolean -> v
                is Int -> v >= 1
                is Long -> v >= 1
                is Float -> v >= 1f
                is Double -> v >= 1.0
                is String -> v.isNotEmpty()
                null -> false
                else -> true
            }
        }

        fun boolNum(b: Boolean): Int = if (b) 1 else 0

        return when (op) {
            "+" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para +")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para +")
                val r = a + b
                if (izq is Int && der is Int) r.toInt() else r
            }

            "-" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para -")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para -")
                val r = a - b
                if (izq is Int && der is Int) r.toInt() else r
            }

            "*" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para *")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para *")
                val r = a * b
                if (izq is Int && der is Int) r.toInt() else r
            }

            "/" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para /")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para /")
                if (b == 0.0) throw RuntimeException("División entre 0")
                a / b
            }

            "^" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para ^")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para ^")
                a.pow(b)
            }

            "%" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para %")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para %")
                if (b == 0.0) throw RuntimeException("Módulo por 0")
                val r = a % b
                if (izq is Int && der is Int) r.toInt() else r
            }

            "==" -> boolNum(izq == der)
            "!!" -> boolNum(izq != der)
            "<" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para <")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para <")
                boolNum(a < b)
            }

            "<=" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para <=" )
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para <=" )
                boolNum(a <= b)
            }

            ">" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para >")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para >")
                boolNum(a > b)
            }

            ">=" -> {
                val a = asDouble(izq) ?: throw RuntimeException("Operando izquierdo no numérico para >=")
                val b = asDouble(der) ?: throw RuntimeException("Operando derecho no numérico para >=")
                boolNum(a >= b)
            }

            "&&" -> boolNum(verdadero(izq) && verdadero(der))
            "||" -> boolNum(verdadero(izq) || verdadero(der))

            else -> throw RuntimeException("Operador binario no soportado: $op")
        }
    }

    override fun visit(nodo: NodoExpresionUnaria?): Any? {
        if (nodo == null) return null
        val op = nodo.operador
        val v = nodo.expresion?.aceptar(this)

        fun verdadero(x: Any?): Boolean {
            return when (x) {
                is Boolean -> x
                is Int -> x >= 1
                is Long -> x >= 1
                is Float -> x >= 1f
                is Double -> x >= 1.0
                is String -> x.isNotEmpty()
                null -> false
                else -> true
            }
        }

        fun asDouble(x: Any?): Double? = when (x) {
            is Int -> x.toDouble()
            is Long -> x.toDouble()
            is Float -> x.toDouble()
            is Double -> x
            else -> null
        }

        return when (op) {
            "-" -> {
                val d = asDouble(v) ?: throw RuntimeException("Operando no numérico para unario -")
                val r = -d
                if (v is Int) r.toInt() else r
            }
            "+" -> {
                val d = asDouble(v) ?: throw RuntimeException("Operando no numérico para unario +")
                if (v is Int) d.toInt() else d
            }
            "~" -> {
                if (verdadero(v)) 0 else 1
            }
            else -> throw RuntimeException("Operador unario no soportado: $op")
        }
    }

    override fun visit(nodo: NodoLiteral?): Any? {
        if(nodo == null) return null
        return when (nodo.tipoLiteral) {
            "number" -> nodo.valor.toIntOrNull()
            "decimal" -> nodo.valor.toDoubleOrNull()
            else -> nodo.valor
        }
    }

    override fun visit(nodo: NodoIdentificador?): String? {
        if (nodo == null) {
            println("null")
            return null
        }
        val id = nodo.nombre

        if (valores.containsKey(id)) {
            val v = valores[id]?.toString()
            println("Valor id: $v para $id")
            return valores[id]?.toString()
        }

        if (specials.containsKey(id)) return id

        throw RuntimeException("Identificador no declarado: '$id'")
    }

    override fun visit(nodo: NodoCadenaCompuesta?): String? {
        if (nodo == null) return null
        val sb = StringBuilder()
        for (parte in nodo.partes) {
            val v = parte.aceptar(this)
            sb.append(v?.toString() ?: "")
        }
        return sb.toString()
    }

    override fun visit(nodo: NodoLlamadaMetodo?): Pregunta? {
        if (nodo == null) return null

        if (nodo.metodo != "draw") {
            throw RuntimeException("Método no soportado: ${nodo.metodo}")
        }

        val objetivo = nodo.objetivo
        val plantilla = specials[objetivo]
            ?: throw RuntimeException("No existe special '${objetivo}'")

        val aridadEsperada = specialAridad[objetivo] ?: contarComodines(plantilla)
        val argsAst = (nodo.argumentos ?: emptyList<NodoExpresion>())
        if (argsAst.size != aridadEsperada) {
            throw RuntimeException(
                "La llamada ${objetivo}.draw(...) esperaba $aridadEsperada argumentos pero recibió ${argsAst.size}"
            )
        }

        val argsEvaluados = argsAst.map { (it as NodoExpresion).aceptar(this) }

        val prevComodines = comodinesActivos
        val prevIndice = indiceComodinActivo
        comodinesActivos = argsEvaluados
        indiceComodinActivo = 0
        try {
            val pregunta = plantilla.aceptar(this) as Pregunta?
            //pregunta?.let { formularioActual?.addElemento(it) }
            return pregunta

        } finally {
            comodinesActivos = prevComodines
            indiceComodinActivo = prevIndice
        }
    }

    override fun visit(nodo: NodoComodin?): Int {
        val activos = comodinesActivos
            ?: throw RuntimeException("Se encontró '?' fuera de una invocación draw")

        if (indiceComodinActivo >= activos.size) {
            throw RuntimeException("Faltan argumentos para resolver '?' (índice $indiceComodinActivo)")
        }
        val valor = activos[indiceComodinActivo]
        indiceComodinActivo++
        return parseInt(valor, 0)
    }

    private fun contarComodines(nodo: NodoAST?): Int {
        if (nodo == null) return 0
        return when (nodo) {
            is NodoPreguntaAbierta -> nodo.atributos?.sumOf { contarComodines(it as? NodoAST) } ?: 0
            is NodoPreguntaSeleccionUnica -> nodo.atributos?.sumOf { contarComodines(it as? NodoAST) } ?: 0
            is NodoPreguntaSeleccionMultiple -> nodo.atributos?.sumOf { contarComodines(it as? NodoAST) } ?: 0
            is NodoPreguntaDesplegable -> nodo.atributos?.sumOf { contarComodines(it as? NodoAST) } ?: 0
            is NodoAtributo -> contarComodines(nodo.valor)

            is NodoComodin -> 1
            is NodoListaExpresiones -> nodo.expresiones?.sumOf { contarComodines(it) } ?: 0
            is NodoCadenaCompuesta -> nodo.partes?.sumOf { contarComodines(it) } ?: 0
            is NodoExpresionBinaria -> contarComodines(nodo.izquierda) + contarComodines(nodo.derecha)
            is NodoExpresionUnaria -> contarComodines(nodo.expresion)

            else -> 0
        }
    }

    private fun recolectarAtributos(lista: List<*>?): Map<String, Any?> {
        val res = mutableMapOf<String, Any?>()
        val attrs = lista ?: emptyList<Any?>()
        for (a in attrs) {
            val ev = (a as? NodoAtributo)?.aceptar(this)
            if (ev is AtributoEvaluado) {
                res[ev.nombre] = ev.valor
            }
        }
        return res
    }

    private fun aplicarAtributosElemento(elem: Elemento, attrs: Map<String, Any?>) {
        // En PKM se usan x/y para width/height.
        attr(attrs, "width", "x")?.let { elem.setWidth(parseInt(it, elem.width)) }
        attr(attrs, "height", "y")?.let { elem.setHeight(parseInt(it, elem.height)) }
        attr(attrs, "styles", "estilos")?.let {
            if (it is Estilos) elem.setEstilos(it)
        }
    }

    private fun attr(attrs: Map<String, Any?>, vararg keys: String): Any? {
        for (k in keys) {
            if (attrs.containsKey(k)) return attrs[k]
        }
        return null
    }

    private fun toStringList(v: Any?): List<String> {
        return when (v) {
            is List<*> -> v.map { it?.toString() ?: "" }
            null -> emptyList()
            else -> listOf(v.toString())
        }
    }

    private fun toIntList(v: Any?): List<Int> {
        return when (v) {
            is List<*> -> v.mapNotNull { parseIntOrNull(it) }
            is String -> parseListaInts(v)
            else -> emptyList()
        }
    }

    private fun parseListaInts(texto: String): List<Int> {
        return texto
            .removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .mapNotNull { it.trim().toIntOrNull() }
    }

    private fun verdadero(v: Any?): Boolean {
        return when (v) {
            is Boolean -> v
            is Int -> v >= 1
            is Long -> v >= 1
            is Float -> v >= 1f
            is Double -> v >= 1.0
            is String -> v.isNotEmpty()
            null -> false
            else -> true
        }
    }

    private fun parseIntOrNull(v: Any?): Int? {
        return when (v) {
            is Int -> v
            is Long -> v.toInt()
            is Double -> v.toInt()
            is Float -> v.toInt()
            is String -> v.trim().toIntOrNull()
            else -> null
        }
    }

    private fun parseInt(v: Any?, default: Int): Int = parseIntOrNull(v) ?: default

    private fun parseColor(v: Any?): Color {
        val s = v?.toString()?.trim() ?: "#000000"
        val c = Color()
        return when {
            s.startsWith("(") && s.endsWith(")") -> {
                // rgb (r,g,b)
                val parts = s.removePrefix("(").removeSuffix(")").split(",")
                if (parts.size == 3) c.setRgb(parts[0].trim(), parts[1].trim(), parts[2].trim())
                c
            }
            s.startsWith("<") && s.endsWith(">") -> {
                // hsl <h,s,l>
                val parts = s.removePrefix("<").removeSuffix(">").split(",")
                if (parts.size == 3) c.setHsl(parts[0].trim(), parts[1].trim(), parts[2].trim())
                c
            }
            s.startsWith("#") || s.matches(Regex("^[0-9A-Fa-f]{6}$")) -> {
                c.setHex(s)
                c
            }
            else -> {
                val hex = when (s.uppercase()) {
                    "WHITE" -> "#FFFFFF"
                    "BLACK" -> "#000000"
                    "RED" -> "#FF0000"
                    "GREEN" -> "#00FF00"
                    "BLUE" -> "#0000FF"
                    "YELLOW" -> "#FFFF00"
                    "CYAN" -> "#00FFFF"
                    "MAGENTA" -> "#FF00FF"
                    else -> "#000000"
                }
                c.setHex(hex)
                c
            }
        }
    }

    private fun parseFuente(v: Any?): Fuente {
        return try {
            Fuente.valueOf((v?.toString() ?: "MONO").trim().uppercase())
        } catch (_: IllegalArgumentException) {
            Fuente.MONO
        }
    }

    private fun inferirTipo(valor: Any?): Tipo {
        return when (valor) {
            is Int, is Long, is Double, is Float -> Tipo.NUMBER
            is String -> Tipo.STRING
            is Boolean -> Tipo.BOOLEAN
            else -> Tipo.ERROR
        }
    }

}