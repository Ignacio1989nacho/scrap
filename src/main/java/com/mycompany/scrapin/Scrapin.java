/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.scrapin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Usuario
 */
public class Scrapin {

    public static void main(String[] args) {
        //System.out.println(Scrapin.getHTML("https://www.gomarex.com.ar/catalogo/")); // ver html completo
//        for (String arg : extraerCodigo()) { // MUESTRA TODOS LOS CODIGOS DE LOS PRODUCTOS DE LA PAGINA
//            System.out.println(arg);
//        }
        // contadorCodigos();
        delCodigoRepetido();
    }

    /**
     * METODO OBTIENE EL CODIGO HTMLO DE LA PAGINA WEB
     *
     * @param url de la pagina
     * @return codigo html
     */
    public static Document getHTML(String url) {
        Document html = null;
        try {
            html = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (Exception e) {
            System.out.println("Error al obtener codigo html");
        }
        return html;
    }

    /**
     * METODO RECORRE CADA UL.CHILDREN, BUSCANDO DENTRO LA ETIQUETA LI.CAT -
     * ITEM A, PARA EXTRAER EL HREF (LINK) PARA LUEGO TRANSFORMARLO EN STRING Y
     * ENVIARLO A UN ARRAYLIST.
     *
     * @return ARRAYLIST<STRING>
     */
    public static ArrayList<String> scrapin() {
        ArrayList<String> urls = new ArrayList();
        Elements articulo = Scrapin.getHTML("https://www.gomarex.com.ar/catalogo/").select("ul.children");
        int contadorProductos = 0;
        for (Element links : articulo) {
            try {
                Elements urlProducto = links.select("li.cat-item a");
                for (Element url : urlProducto) {
                    String u = url.attr("href");// selecciona todos los href
                    Document htmlProducto = Scrapin.getHTML(u);
                    urls.add(u);
                    contadorProductos++;
                    System.out.println(contadorProductos);
                }
            } catch (Exception e) {
                System.out.println("ERROR AL ENTRAR AL UL");
            }
        }
        System.out.println("TOTAL: " + urls.size());
        return urls;
    }

    /**
     * METODO LLAMA AL METODO SCRAPIN, QUE CONTIENE UN ARRAYLIST DE LINKS, PARA
     * LUEGO RECORRERLOS Y EXTRAER EL CODIGO DENTRO DE LA ETIQUETA
     * LI.PRODUCT-SKU, DE CADA LINK. EL MISMO TRANSFORMA ESE DATO EN STRING Y LO
     * ENVIA A UN ARRAYLIST
     *
     * @return ARRAYLIST<STRING> CON LOS CODIGOS DE CADA PRODUCTO DEL CATALOGO.
     * AUTOMOTORES
     */
    public static ArrayList<String> extraerCodigo() {
        ArrayList<String> codigos = new ArrayList();
        for (String s : scrapin()) {
            Elements art = Scrapin.getHTML(s).select("li.product-sku");
            for (Element link : art) {
                Elements urlProducto = link.select("span");
                String codigoLimpio = urlProducto.text();
                codigos.add(codigoLimpio);
            }
        }
        return codigos;
    }

    /**
     * METODO MUESTRA LA CANTIDAD DE CODIGOS EXTRAIDOS DESDE LA WEB, PARA
     * CUANTIFICAR LA CANTIDAD DE PRODUCTOS OFRECIDOS, DESDE UN ARCHIVO TXT, EN EL
     * ESCRITORIO.
     */
    public static ArrayList<String> contadorCodigos() {
        ArrayList<String> totalCodigos = new ArrayList();
        try {
            BufferedReader entrada = new BufferedReader(new FileReader("C:\\Users\\Usuario\\Desktop\\CODIGO_ARTICULO_GM.txt"));//crearlo en el escritorio con los datos obtenidos anteriormente
            String lectura;
            while ((lectura = entrada.readLine()) != null) {
                totalCodigos.add(lectura);
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(System.out);
        }
        //System.out.println(totalCodigos.size());
        return totalCodigos;
    }

    /**
     * METODO LLAMA AL METODO CONTADOR DE CODIGOS, QUE DEVUELVE UN ARRAYLIST DE
     * CODIGOS, Y GUARDA LOS MISMOS PERO AHORA EN UN HASHSET, PARA CALCULAR LOS
     * CODIGOS REPETIDOS. TAMBIEN MUESTRA LOS CODIGOS REPETIDOS Y CUANTAS VECES
     * APARECEN.
     */
    public static void delCodigoRepetido() {

        ArrayList<String> cc = contadorCodigos();
        HashSet<String> codigoSinRepetir = new HashSet();
        for (String string : cc) {
            codigoSinRepetir.add(string);
        }
        
        int cont = 0;
        for (String string : cc) {
            for (int i = 0; i < cc.size(); i++) {
                if (string.equals(cc.get(i))) {
                    cont++;
                }
            }
            if (cont > 1) {
                System.out.println("el numero " + string + " aparace " + (cont-1) + " veces repetido.");
                
            }
            cont = 0;
        }
        System.out.println("Total codigos: " + cc.size());
        System.out.println("Codigos sin repetir: " + codigoSinRepetir.size());
        //System.out.println("Codigo vuelto a poner en un arraylist: " + cc.size());
        System.out.println("codigo repetidos en total: "+ (cc.size()-codigoSinRepetir.size()));
    }
}
