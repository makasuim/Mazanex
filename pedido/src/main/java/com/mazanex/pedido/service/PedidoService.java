package com.mazanex.pedido.service;

import com.mazanex.pedido.model.Pedido;
import com.mazanex.pedido.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String INVENTARIO_URL = "http://localhost:8082/inventario/productos";

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPedido(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido crearPedido(Pedido pedido) {
        pedido.setTotal(calcularTotal(pedido));

        if (pedido.getEstado() == null || pedido.getEstado().isBlank()) {
            pedido.setEstado("CREADO");
        }

        // Guardar pedido en BD
        Pedido guardado = pedidoRepository.save(pedido);

        // Descontar stock por cada producto comprado
        pedido.getProductos().forEach(item -> {
            String url = INVENTARIO_URL + "/" + item.getProductoId() + "/descontar/" + item.getCantidad();
            restTemplate.put(url, null);
        });

        return guardado;
    }

    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setClienteId(pedidoActualizado.getClienteId());
            pedido.setClienteNombre(pedidoActualizado.getClienteNombre());
            pedido.setEstado(pedidoActualizado.getEstado());
            pedido.setProductos(pedidoActualizado.getProductos());
            pedido.setTotal(calcularTotal(pedidoActualizado));
            return pedidoRepository.save(pedido);
        }).orElse(null);
    }

    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    private BigDecimal calcularTotal(Pedido pedido) {
        if (pedido.getProductos() == null) return BigDecimal.ZERO;

        return pedido.getProductos().stream()
                .map(p -> p.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(p.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
