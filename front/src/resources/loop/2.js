export const task = `
-> statement
    with resolved: "System.out.println(5)"
    with for iteration: "0"
    log info: "Wywołanie metody println ze zmienną x w 0 iteracji for"
-> statement
    with resolved: "System.out.println(10)"
    with for iteration: "1"
    log info: "Wywołanie metody println ze zmienną x w 1 iteracji for"
-> statement
    with resolved: "System.out.println(15)"
    with for iteration: "2"
    log info: "Wywołanie metody println ze zmienną x w 2 iteracji for"
-> statement
    with resolved: "System.out.println(20)"
    with for iteration: "3"
    log info: "Wywołanie metody println ze zmienną x w 3 iteracji for"
-> statement
    with resolved: "System.out.println(25)"
    with for iteration: "4"
    log info: "Wywołanie metody println ze zmienną x w 4 iteracji for"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int x = 0;


    }
}`;

export const description = `Pętla <b>for</b> jest najczęściej wykorzystywana jeżeli znana jest dokładna ilość iteracji, czyli powtórzeń danego kawałka kodu. Konstrukcja wygląda następująco:
<java>
for (wyrażenie1; wyrażenie2; wyrażenie3) {
  // kod
}
</java>
Wyrażenie1 jest wykonywane tylko raz przed wykonaniem bloku kodu. Najczęściej służy ono do zainicjowania jakiegoś indeksu, który następnie będzie modyfikowany i używany przy kolejnych iteracjach.
Wyrażenie2 używane jest do określenia warunku zatrzymania pętli. Jest on sprawdzany przed każdym wykonaniem bloku kodu.
Wyrażenie3 jest wykonywane po każdym przejściu przez blok kodu. Najczęściej zwiększa się w tym miejscu wartość indeksu.
Należy podkreślić jednak, że każde z powyższych wyrażeń jest opcjonalne, jednak pominięcie warunku zatrzymania pętli może powodować pętlę nieskończoną.`;

export const instruction = `Wypisując na ekran (println) zmienną x, przy użyciu pętli for wyświetl kolejne wielokrotności 5 (5, ..., 25).`;

export const hasNext = true;