export const task = `
-> statement
    with text: "int r1 = (a+b)*c/2"
    with resolved: "int r1 = 8"
    log info: "Instrukcja r1 o wartości 8"
-> statement
    with text: "double r2 = d%e"
    with resolved: "double r2 = 2.0"
    log info: "Instrukcja r2 o wartości 2.0"
-> statement
    with text: "int r3 = -f+(-g)*h"
    with resolved: "int r3 = -11"
    log info: "Instrukcja r3 o wartości -11"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        
        int r1 = (a+b)*c/2; // 8

        double r2 = d%e; // 2.0

        int r3 = -f+(-g)*h; // -11
    }
}`;

export const description = `W języku Java dozwolone są wszelakie operacje matematyczne. W przypadku działań na liczbach można się posłużyć takimi operandami: +, -, *, /, % (reszta z dzielenia). Oczywiście kolejność działań jest zgodna z zasadami matematycznymi, a modyfikować ją można poprzez używanie ( ). Należy dodać, że w działaniach mogą być wykorzystywane zarówno literały jak i zdefiniowane zmienne liczbowe.`;

export const instruction = `Zdefiniuj odpowiednie zmienne, aby istnijące wyrażenia dawały rezultaty takie jak w komentarzach.`;

export const hasNext = true;