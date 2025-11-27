package com.mazanex.pedido.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que hace la compra (id de tu microservicio auth)
    private Long clienteId;

    private String clienteNombre;

    private LocalDateTime fechaPedido = LocalDateTime.now();

    // Estados típicos: CREADO, PAGADO, ENVIADO, CANCELADO, etc.
    private String estado = "CREADO";

    private BigDecimal total;

    @ElementCollection
    @CollectionTable(name = "pedido_productos", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<ItemProducto> productos;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemProducto {
        private Long productoId;
        private String nombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}
