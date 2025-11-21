Cambios !!
REQUERIMIENTOS !! 
1. Tener la base de datos 
2. Iniciar como Admin "jlopez - Admin2025!" debido a que la seguridad ta muy dura
-- AdminMovimientoController ->

    @GetMapping("/recientes")
    public ResponseEntity<List<MovimientoResponseDTO>> listarRecientes() {
        return ResponseEntity.ok(movimientosServices.listarRecientes());
    }


-- MovimientosServices -> 
    public List<MovimientoResponseDTO> listarRecientes() {
        List<Movimiento> movimientos = movimientoRepository.findAllWithRelations();
        System.out.println("Movimientos encontrados: " + movimientos.size());

        return movimientoRepository.findAll().stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().toString());
            dto.setComentario(mov.getComentario());
            dto.setFecha(mov.getFecha());
            dto.setUsuarioNombre(mov.getUsuario().getNombre());
            dto.setBodegaOrigenNombre(
                    mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null
            );
            dto.setBodegaDestinoNombre(
                    mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null
            );

            dto.setDetalles(
                    mov.getDetalles().stream().map(det -> {
                        MovimientoResponseDTO.DetalleDTO detDto = new MovimientoResponseDTO.DetalleDTO();
                        detDto.setProductoNombre(det.getProducto().getNombre());
                        detDto.setCantidad(det.getCantidad());
                        return detDto;
                    }).collect(Collectors.toList())
            );

            return dto;
        }).collect(Collectors.toList());
    }

    "Esta es para ver el resumen, osea, el reporte de los movimientos
    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        Map<String, Object> resumen = new HashMap<>();

        Long totalBodegas = bodegaRepository.count();
        Long totalProductos = productoRepository.count();

        LocalDate hoy = LocalDate.now();
        Long movimientosHoy = movimientoRepository.countByFechaBetween(
                hoy.atStartOfDay(),
                hoy.plusDays(1).atStartOfDay()
        );

        resumen.put("totalBodegas", totalBodegas);
        resumen.put("totalProductos", totalProductos);
        resumen.put("movimientosHoy", movimientosHoy);

        return resumen;
    }