export const task = `
-> variable
    with text: "String s1"
    log info: "Zmienna s1 typu String"
-> variable
    with text: "String s2"
    log info: "Zmienna s2 typu String"
-> statement
    with text: "boolean b1 = s1 == s2"
    with resolved: "boolean b1 = false"
    log info: "Zmienna b1 o oczekiwanej wartości s1 == s2"
-> statement
    with text: "boolean b2 = s1.equals(s2)"
    with resolved: "boolean b2 = true"
    log info: "Zmienna b2 o oczekiwanej wartości s1.equals(s2)"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

        boolean b1 = s1 == s2;          // false
        boolean b2 = s1.equals(s2);     // true
    }
}`;

export const description = `Dotychczas porównywane były typy proste, dla który operator == bierze pod uwagę reprezentowane wartości, dzięki czemu porównanie w ten sposób dwóch zmiennych typu int daje oczekiwany rezultat. Należy jednak podkreślić, iż każdy z typów prostych ma odpowiadający mu typ obiektowy, który posiada dedykowane mu dodatkowe funkcje. Dla poznanych już typów prymitywnych odpowiadające im typy złożone to: Character, Byte, Short, Integer, Long, Float, Double oraz Boolean. Innym tego rodzaju typem jest również, używany wcześniej String.
W odróżnieniu od typów prymitywnych operator == dla typów obiektowych porównuje referencje czyli pewnego rodzaju wskaźniki na miejsce w pamięci, gdzie znajduje się dany obiekt. Przez takie zachowanie, porównanie dla przykładu dwóch zmiennych typu Integer, o tej samej wartości liczbowej, może dać nieoczekiwany rezultat. Dlatego też, do porównywania typów obiektowych używa się metody equals(), która dla powyższych typów została zaimplementowana do porównywania reprezentowanych wartości.
`;

export const instruction = `Zdefiniuj dwie zmienne s1 oraz s2, które będą przechowywać ciągi znaków oraz będą spełniały widoczne porównania.`;

export const hasNext = true;