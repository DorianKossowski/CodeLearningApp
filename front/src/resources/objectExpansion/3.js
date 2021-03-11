export const task = `
-> class
    with field
        with modifiers:
        with type: "int"
        with name: "field"
    with constructor
        with name: "MyClass"
        with args: { "int", - }
-> statement
    with text: "MyClass myClass1 = new MyClass(0)"
    in method: "main"
    log info: "Definicja zmiennej myClass1"
-> statement
    with text: "MyClass myClass2 = new MyClass(1)"
    in method: "main"
    log info: "Definicja zmiennej myClass2"
-> statement
    with text: "System.out.println(myClass1.getDouble())"
    with resolved: "System.out.println(0)"
    in method: "main"
    log info: "Wywołanie println z wywołaniem metody (na obiekcie myClass1) jako parametr"
-> statement
    with text: "System.out.println(myClass2.getDouble())"
    with resolved: "System.out.println(2)"
    in method: "main"
    log info: "Wywołanie println z wywołaniem metody (na obiekcie myClass2) jako parametr"
`;

export const input = `public class MyClass {
    int field;

    public static void main(String[] args){
        MyClass myClass1 = new MyClass(0);
        MyClass myClass2 = new MyClass(1);
        System.out.println();
        System.out.println();
    }

    MyClass(int value) {
        field = value;
    }

}`;

export const description = `Wywoływanie metod na konkretnych instancjach niewiele różni się od wołania metod statycznych. Różnica sprowadza się do tego iż, konieczny jest obiekt, na którym za pośrednictwem kropki, wołana jest metoda:
<java>
NAZWA_OBIEKTU.NAZWA_METODY(OPCJONALNE_PARAM);
</java>
Dzięki tego typu metodom, możliwe jest korzystanie z pól instancyjnych bez konieczności przekazywania ich za pomocą dodatkowych parametrów. Pozwala to ograniczyć dodatkowe, zbędne zmienne i przy okazji zachować czytelność kodu.`;

export const instruction = `Nie zmieniając istniejącego kodu, uzupełnij go o brakujące elementy. Napisz metodę bezargumentową getDouble, która będzie zwracała podwojoną wartość pola field. Przekaż bezpośrednio do widocznych funkcji println wywołania tejże metody na obiektach myClass1 oraz myClass2.`;

export const hasNext = true;