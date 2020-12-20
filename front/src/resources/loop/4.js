export const task = `
-> statement
    with resolved: "suma = 1"
    with do while iteration: "0"
    log info: "Przypisanie do zmiennej suma w 0 iteracji do while"
-> statement
    with resolved: "suma = 3"
    with do while iteration: "1"
    log info: "Przypisanie do zmiennej suma w 1 iteracji do while"
-> statement
    with resolved: "suma = 6"
    with do while iteration: "2"
    log info: "Przypisanie do zmiennej suma w 2 iteracji do while"
-> statement
    with text: "System.out.println(suma)"
    with resolved: "System.out.println(6)"
    log info: "Wywołanie metody println ze zmienną suma"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int suma = 0;

        int i=1;
        while(i<=3) {
            suma = suma + i;
            i = i+1;
        }

        System.out.println(suma);
    }
}`;

export const description = `Bardzo podobną pętlą do poprzedniej jest <b>do while</b>:
<java>
do {
  // kod
} while (condition);
</java>
Różnica jest taka, że kod wykona się zawsze conajmniej raz, niezależnie czy warunek jest spełniony czy nie. Kolejne iteracje natomiast są wykonywane tylko przy spełnionym warunku. Należy również w tym przypadku pamiętać o konieczności postawienia średnika kończącego instrukcję.`;

export const instruction = `Przerób widoczny kod na taki, który używa pętli do while. Logika programu ma zostać bez zmian.`;

export const hasNext = false;