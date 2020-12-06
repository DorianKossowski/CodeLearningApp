export const task = `
-> variable
    with text: "int i"
    log info: "Zmienna i typu int"
-> variable
    with text: "char c"
    log info: "Zmienna c typu char"
-> statement
    with text: "boolean result = i == c"
    with resolved: "boolean result = true"
    log info: "Zmienna result o oczekiwanej wartości i == c"
-> statement
    with text: "boolean b1 = a >= b"
    with resolved: "boolean b1 = true"
    log info: "Zmienna b1 o oczekiwanej wartości a >= b"
-> statement
    with text: "boolean b2 = a*2 < b+1"
    with resolved: "boolean b2 = true"
    log info: "Zmienna b2 o oczekiwanej wartości a*2 < b+1"
-> statement
    with text: "boolean b3 = a%2 != 0"
    with resolved: "boolean b3 = true"
    log info: "Zmienna b3 o oczekiwanej wartości a%2 != 0"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

        boolean result = i == c;    // true

        boolean b1 = a >= b;    // true

        boolean b2 = a*2 < b+1; // true

        boolean b3 = a%2 != 0;  // true
    }
}`;

export const description = `W celu porównywania ze sobą dwóch wartości korzysta się z następujących operatorów:
<b>==</b> - równy
<b>!=</b> - różny
<b>>=</b> - większy lub równy
<b><=</b> - mniejszy lub równy
<b>></b>  -  większy
<b><</b>  - mniejszy
Wynikiem użycia powyższych operatorów jest wartość logiczna <b>true</b> lub <b>false</b>.
`;

export const instruction = `Zdefiniuj zmienne i orac c, które są kolejno typów int oraz char, w taki sposób aby wynik operacji sprawdzenia równości był prawdą. (podpowiedź: system kodowania znaków ASCII).
Zdefiniuj i dopasuj zmienne a oraz b, tak aby operacje logiczne dawały wyniki jak w komentarzach.`;

export const hasNext = true;