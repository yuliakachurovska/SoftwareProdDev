package task04

class Employee(
    val firstName: String,
    val lastName: String,
    val position: String
)

fun main() {

    val defaultName = "Олександр"
    val defaultSurname = "Винниченко"
    val defaultPosition = "Письменник"

    val emp1 = Employee(defaultName, defaultSurname, defaultPosition)
    val emp2 = Employee(defaultName, defaultSurname, defaultPosition)
    val emp3 = emp1

    println(emp1)

    println(emp1 === emp2)
    println(emp1 == emp2)
    println(emp1 === emp3)
    println(emp1 == emp3)


    data class EmployeeData(
        val firstName: String,
        val lastName: String,
        val position: String
    )

    val employee1 = EmployeeData(defaultName, defaultSurname, defaultPosition)
    val employee2 = EmployeeData(defaultName, defaultSurname, defaultPosition)

    println(employee1)
    println(employee1 == employee2)

    val (name, surname, pos) = employee1

    println(name)
    println(surname)
    println(pos)

    val employee3 = employee1.copy(position = "Старший програміст")

    println(employee3)


    // Додаткове завдання з bonus

    data class EmployeeWithBonus(
        val firstName: String,
        val lastName: String,
        val position: String
    ) {
        var bonus: Int = 0
    }

    val bonusEmployee1 = EmployeeWithBonus(
        defaultName,
        defaultSurname,
        defaultPosition
    )

    val bonusEmployee2 = EmployeeWithBonus(
        defaultName,
        defaultSurname,
        defaultPosition
    )

    bonusEmployee1.bonus = 1000
    bonusEmployee2.bonus = 2000

    println(bonusEmployee1 == bonusEmployee2)


    class EmployeeManual(
        val firstName: String,
        val lastName: String,
        val position: String
    ) {

        override fun toString(): String {
            return "Employee(firstName='$firstName', lastName='$lastName', position='$position')"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is EmployeeManual) return false

            return firstName == other.firstName &&
                    lastName == other.lastName &&
                    position == other.position
        }

        override fun hashCode(): Int {
            var result = firstName.hashCode()
            result = 31 * result + lastName.hashCode()
            result = 31 * result + position.hashCode()
            return result
        }

        operator fun component1(): String {
            return firstName
        }

        operator fun component2(): String {
            return lastName
        }

        operator fun component3(): String {
            return position
        }

        fun copy(
            firstName: String = this.firstName,
            lastName: String = this.lastName,
            position: String = this.position
        ): EmployeeManual {
            return EmployeeManual(firstName, lastName, position)
        }
    }

    val manualEmp1 = EmployeeManual(
        defaultName,
        defaultSurname,
        defaultPosition
    )

    val manualEmp2 = EmployeeManual(
        defaultName,
        defaultSurname,
        defaultPosition
    )

    println(manualEmp1)
    println(manualEmp1 == manualEmp2)

    val (manualName, manualSurname, manualPosition) = manualEmp1

    println(manualName)
    println(manualSurname)
    println(manualPosition)

    val manualEmp3 = manualEmp1.copy(
        position = "Старший програміст"
    )

    println(manualEmp3)
}