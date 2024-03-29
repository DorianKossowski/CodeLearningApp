export const task = `
-> statement
    with resolved: "System.out.println(0)"
    with for iteration: "0"
    log info: "Wywołanie metody println w 0 iteracji for"
-> statement
    with resolved: "System.out.println(2)"
    with for iteration: "1"
    log info: "Wywołanie metody println w 1 iteracji for"
-> statement
    with resolved: "System.out.println(2)"
    with while iteration: "0"
    log info: "Wywołanie metody println w 0 iteracji while"
-> statement
    with resolved: "System.out.println(3)"
    with while iteration: "1"
    log info: "Wywołanie metody println w 1 iteracji while"
-> statement
    with resolved: "System.out.println(4)"
    with do while iteration: "0"
    log info: "Wywołanie metody println w 0 iteracji do while"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        for(int i=0; i<3; i = i+1) {
            System.out.println(i*2);
            if(i == 1) break;
        }

        int i=0;
        while(i<2) {
            System.out.println(i+2);
            i = i+1;
        }

        do {
            System.out.println(i+2);
        } while(i<2);
    }
}`;

export const description = `Kolejnym często stosowanym w programowaniu mechanizmem są pętle. Te podstawowe konstrukcje pozwalają powtarzać określoną logikę tak długo, aż spełniony jest pewien warunek. Język Java udostępnie m.in. pętle <b>for</b>, <b>while</b> oraz <b>do while</b>.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje, które poznasz w tym rozdziale.`;

export const hasNext = true;