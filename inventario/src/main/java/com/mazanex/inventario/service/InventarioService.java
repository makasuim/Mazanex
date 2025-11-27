package com.mazanex.inventario.service;

import com.mazanex.inventario.model.Producto;
import com.mazanex.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Agregar lista de productos (POST masivo)
    public List<Producto> agregarProductos(List<Producto> productos) {
        return productoRepository.saveAll(productos);
    }

    // Agregar un solo producto
    public Producto agregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Actualizar producto por ID
    public Producto actualizarProducto(Long id, Producto datos) {
        return productoRepository.findById(id)
                .map(existente -> {
                    existente.setNombre(datos.getNombre());
                    existente.setDescripcion(datos.getDescripcion());
                    existente.setStock(datos.getStock());
                    existente.setPrecio(datos.getPrecio());
                    existente.setCategoria(datos.getCategoria());
                    existente.setImagen(datos.getImagen());
                    return productoRepository.save(existente);
                })
                .orElse(null);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto restarStock(Long id, int cantidad) {
        return productoRepository.findById(id)
                .map(p -> {
                    if (p.getStock() < cantidad) {
                        return null;
                    }
                    p.setStock(p.getStock() - cantidad);
                    return productoRepository.save(p);
                })
                .orElse(null);
    }

    public boolean descontarStock(Long id, int cantidad) {
        return productoRepository.findById(id).map(producto -> {
            if (producto.getStock() < cantidad) {
                return false;
            }
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
            return true;
        }).orElse(false);
    }
}
