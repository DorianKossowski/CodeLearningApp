export const task = `
-> statement
    with resolved: "suma = 1"
    with while iteration: "0"
    log info: "Przypisanie do zmiennej suma w 0 iteracji while"
-> statement
    with resolved: "suma = 3"
    with while iteration: "1"
    log info: "Przypisanie do zmiennej suma w 1 iteracji while"
-> statement
    with resolved: "suma = 6"
    with while iteration: "2"
    log info: "Przypisanie do zmiennej suma w 2 iteracji while"
-> statement
    with text: "System.out.println(suma)"
    with resolved: "System.out.println(6)"
    log info: "Wywołanie metody println ze zmienną suma"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int suma = 0;

        //miejsce na pętlę while

        System.out.println(suma);
    }
}`;

export const description = `Kolejną pętlą jest instrukcja <b>while</b>. Składa się ona tylko z warunku, który jest sprawdzany przed każdą iteracją oraz samego bloku kodu:
<java>
while (warunek) {
  // kod
}
</java>
Jeżeli określony warunek jest prawdziwy, kod zawarty wewnątrz <b>while</b> się wykona, w przeciwnym wypadku program przechodzi do instrukcji występującej po pętli.`;

export const instruction = `Dopisz odpowiednią pętlę while, w której ciele jedną z instrukcji będzie: suma = suma + i;, a liczba jej interacji wyniesie 3. Rezultatem funkcji main ma być wypisanie na ekran liczby 20.`;

export const hasNext = true;