export const task = `
-> statement
    with text: "boolean b1 = a > b && b.equals(c)"
    with resolved: "boolean b1 = true"
    log info: "Zmienna b1 o oczekiwanej wartości a > b && b.equals(c)"
-> statement
    with text: "boolean b2 = false || a - b != 1"
    with resolved: "boolean b2 = false"
    log info: "Zmienna b2 o oczekiwanej wartości false || a - b != 1"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

        boolean b1 = a > b && b.equals(c);    // true

        boolean b2 = false || a - b != 1;     // false
    }
}`;

export const description = `W Javie istnieją również operacje logiczne. Jedne z nich to koniunkcja &&, która daje prawde tylko wtedy, kiedy oba warunki są spełnione oraz alternatywa ||, która daje fałsz tylko wtedy, gdy oba warunki są fałszywe. Operacje te są przydatne w syutacji, gdy potrzeba jest złączenia kilku warunków. Przykładem może być sprawdzenie czy liczba jest jednocześnie dodatnia i parzysta.
`;

export const instruction = `Zdefiniuj zmienne a, b oraz c tak, aby wartości b1 oraz b2 były równe tym w komentarzach.`;

export const hasNext = false;