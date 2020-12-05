export const task = `
-> statement
    with text: "System.out.println(\\"ELSE a<b\\")"
    is in else
    log info: "Wywołanie metody println z napisem: ELSE a<b"
-> statement
    with text: "System.out.println(\\"b != { 0,1 }\\")"
    with switch expression: "b"
    with switch label: "default"
    log info: "Wywołanie metody println z napisem: b != { 0,1 }"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int a = 1;
        int b = 2;

        if(a > b) {
            System.out.println("IF a>b");
        } else if(a == b) {
            System.out.println("ELSE IF a==b");
        } else {
            System.out.println("ELSE a<b");
        }

        switch (b) {
            case 0:
                System.out.println("b == 0");
                break;
            case 1:
                System.out.println("b == 1");
                break;
            default:
                System.out.println("b != { 0,1 }");
        }
    }
}`;

export const description = `Często w aplikacjach występują miejsca w kodzie, w których podejmowana jest decyzja, która część logiki zostanie wykonana. Podstawowym przykładem może być sprawdzenie parzystości liczby i jeżeli jest ona parzysta wykonanie innych działań aniżeli dla liczby nieparzystej. Do takich operacji używa się instrukcji warunkowych, czyli po prostu instrukcji wyboru. Język Java udostępnia dwa tego typu mechanizmy:
- instrukcja if
- instrukcja switch`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje, które poznasz w tym rozdziale.`;

export const hasNext = true;