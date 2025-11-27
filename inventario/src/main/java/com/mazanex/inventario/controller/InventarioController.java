package com.mazanex.inventario.controller;

import com.mazanex.inventario.dto.CrearProductoRequest;
import com.mazanex.inventario.model.Producto;
import com.mazanex.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(inventarioService.listarProductos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        return inventarioService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST de producto único (para la APP Android)
    @PostMapping("/productos")
    public ResponseEntity<Producto> crear(@RequestBody CrearProductoRequest req) {
        Producto p = new Producto();
        p.setNombre(req.getNombre());
        p.setDescripcion(req.getDescripcion());
        p.setStock(req.getStock());
        p.setPrecio(req.getPrecio());
        p.setCategoria(req.getCategoria());
        p.setImagen(req.getImagen());

        Producto guardado = inventarioService.agregarProducto(p);
        return ResponseEntity.ok(guardado);
    }

    // 🔁 POST masivo (si lo sigues usando en otro lado)
    @PostMapping("/productos/lote")
    public ResponseEntity<List<Producto>> agregarLote(@RequestBody List<Producto> productos) {
        return ResponseEntity.ok(inventarioService.agregarProductos(productos));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = inventarioService.actualizarProducto(id, producto);
        return actualizado != null
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/productos/{id}/restar/{cantidad}")
    public ResponseEntity<Producto> restarStock(
            @PathVariable Long id,
            @PathVariable int cantidad
    ) {
        Producto actualizado = inventarioService.restarStock(id, cantidad);
        if (actualizado == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/productos/{id}/descontar/{cantidad}")
    public ResponseEntity<String> descontarStock(
            @PathVariable Long id,
            @PathVariable int cantidad
    ) {
        boolean ok = inventarioService.descontarStock(id, cantidad);
        if (!ok) {
            return ResponseEntity.badRequest().body("Stock insuficiente");
        }
        return ResponseEntity.ok("Stock actualizado");
    }
}
