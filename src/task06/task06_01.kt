package task06


data class Address(
    val street: String,
    val city: String?,
    val postalCode: String?
)

data class Client(
    val id: Int,
    val name: String,
    val email: String?,
    val address: Address?,
    val extraData: Any?
)

val clientList = listOf(
    Client(
        1, "Олена", "olena@example.com",
        Address("вул. Саксаганського, 10", "Київ", "01033"),
        "VIP-клієнт"
    ),
    Client(
        2, "Богдан", null,
        Address("вул. Городоцька, 45", "Львів", null),
        42
    ),
    Client(
        3, "Марія", "maria@example.com",
        null, null
    ),
    Client(
        4, "Дмитро", null,
        Address("вул. Соборна, 1", null, null),
        "Очікує дзвінка"
    )
)


fun getShippingLabel(client: Client): String {
    if (client.address == null) {
        return "Самовивіз: Клієнт ${client.name} не надав адреси"
    }

    val city = client.address.city ?: "Місто не вказано"
    val postalCode = client.address.postalCode ?: "Індекс невідомий"

    return "${client.address.street}, $city, $postalCode"
}


fun printClientNote(client: Client) {
    val note = client.extraData as? String
        ?: "Додаткові примітки відсутні"

    println("Клієнт ${client.name} -> Примітка: $note")
}


fun getClientEmailOrThrow(client: Client): String {
    return client.email
        ?: throw IllegalArgumentException(
            "Клієнт з ID ${client.id} не має електронної пошти!"
        )
}


fun forceGetPostalCode(client: Client): String {
    return client.address!!.postalCode!!
}

fun main() {
    println("--- Завдання 1 ---")
    println("Адреса Олена: ${getShippingLabel(clientList[0])}")
    println("Адреса Богдан: ${getShippingLabel(clientList[1])}")
    println("Адреса Марія: ${getShippingLabel(clientList[2])}")

    println()

    println("--- Завдання 2 ---")
    clientList.forEach {
        printClientNote(it)
    }

    println()

    println("--- Завдання 3 ---")
    val emails = clientList.map { it.email }.filterNotNull()
    val shortestEmailLength = emails.minOfOrNull { it.length }

    println("База email для розсилки: $emails")
    println("Довжина найкоротшого email: ${shortestEmailLength}")

    println()

    println("--- Завдання 4 ---")
    try {
        println(getClientEmailOrThrow(clientList[1]))
    } catch (e: IllegalArgumentException) {
        println("Перехоплено виняток: ${e.message}")
    }

    println()

    println("--- Завдання 5 ---")
    println("Індекс клієнта Олена: ${forceGetPostalCode(clientList[0])}")

    try {
        forceGetPostalCode(clientList[1])
    } catch (e: NullPointerException) {
        println("Перехоплено очікуваний NPE: ${e.message}")
    }
}

/*
Оператор !! припустимий лише тоді, коли ми точно знаємо,
що значення не null, і це гарантовано логікою програми.
У звичайному коді краще використовувати безпечні виклики
або перевірки, щоб уникати NullPointerException.
*/
