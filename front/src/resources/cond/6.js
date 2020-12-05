export const task = `
-> statement
    with resolved: "System.out.println(\\"a nie jest 0 i 1\\")"
    with switch expression: "a"
	with switch label: "default"
    log info: "Wywołanie metody println wewnątrz default"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int a = ;

        switch(a) {
            case 0:
                System.out.println("a == 0");
                break;
            case 1:
                System.out.println("a == 1");
        }
    }
}`;

export const description = `W przypadku gdy konieczne jest obsłużenie wyrażenia, do którego nie pasował żaden z case'ów, należy skorzystać ze słowa kluczowego <b>default</b>. Zasada działania tego elementu jest analogiczna jak ze słowem kluczowym case, jednak nie przyjmuje on żadnej konkretnej wartości.
<java>
switch(wyrażenie) {
  case x:
    // blok kodu
    break;
  default:
	// blok kodu
}
</java>
Jest to element opcjonalny i warto mieć również na uwadze, że nie jest konieczne aby znajdował się on na końcu instrukcji switch.`;

export const instruction = `Uzupełnij kod o brakujące elementy, tak aby napis "a nie jest 0 i 1" został wypisany (println) z bloku należącego do default.`;

export const hasNext = false;