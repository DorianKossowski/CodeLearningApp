export const task = `
-> statement
    with text: "boolean b1 = i == d"
    with resolved: "boolean b1 = false"
    log info: "Zmienna b1 o oczekiwanej wartości i == d"
-> statement
    with text: "boolean b2 = i != d"
    with resolved: "boolean b2 = true"
    log info: "Zmienna b2 o oczekiwanej wartości i != d"
-> statement
    with text: "boolean b3 = i >= d"
    with resolved: "boolean b3 = false"
    log info: "Zmienna b3 o oczekiwanej wartości i >= d"
-> statement
    with text: "boolean b4 = i <= d"
    with resolved: "boolean b4 = true"
    log info: "Zmienna b4 o oczekiwanej wartości i <= d"
-> statement
    with text: "boolean b5 = i > d"
    with resolved: "boolean b5 = false"
    log info: "Zmienna b5 o oczekiwanej wartości i > d"
-> statement
    with text: "boolean b6 = i < d"
    with resolved: "boolean b6 = true"
    log info: "Zmienna b6 o oczekiwanej wartości i < d"
-> statement
    with text: "boolean b7 = iObj1 == iObj2"
    with resolved: "boolean b7 = false"
    log info: "Zmienna b7 o oczekiwanej wartości iObj1 == iObj2"
-> statement
    with text: "boolean b8 = iObj1.equals(iObj2)"
    with resolved: "boolean b8 = true"
    log info: "Zmienna b8 o oczekiwanej wartości iObj1.equals(iObj2)"
-> statement
    with text: "boolean b9 = i < d && false"
    with resolved: "boolean b9 = false"
    log info: "Zmienna b9 o oczekiwanej wartości i < d && false"
-> statement
    with text: "boolean b10 = i < d || false"
    with resolved: "boolean b10 = true"
    log info: "Zmienna b10 o oczekiwanej wartości i < d || true"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int i = 5;
        double d = 10;

        boolean b1 = i == d;    // false
        boolean b2 = i != d;    // true
        boolean b3 = i >= d;    // false
        boolean b4 = i <= d;    // true
        boolean b5 = i > d;     // false
        boolean b6 = i < d;     // true

        Integer iObj1 = 128;
        Integer iObj2 = 128;
        boolean b7 = iObj1 == iObj2;        // false
        boolean b8 = iObj1.equals(iObj2);   // true

        boolean b9 = i < d && false;         // false
        boolean b10 = i < d || false;        // true
    }
}`;

export const description = `Kluczowym elementem każdego języka programowania są operacje porównywania wartości zmiennych. W tym rozdziale pokazane zostaną dostępne w Javie operatory do tego typu zadań. 
Poruszony zostanie również temat sprawdzania równości typów złożonych. Otrzymasz odpowiedź na pytanie: Kiedy należy użyć operatora == a kiedy metody equals?
W rozdziale pokazane zostaną również operacje logiczne, dzięki czemu dowiesz się jak działają operatory && oraz ||.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje, które poznasz w tym rozdziale.`;

export const hasNext = true;