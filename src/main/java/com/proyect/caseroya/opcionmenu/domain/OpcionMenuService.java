package com.proyect.caseroya.opcionmenu.domain;

import com.proyect.caseroya.opcionmenu.dto.OpcionMenuDto;
import com.proyect.caseroya.opcionmenu.infrastructure.OpcionMenuRepository;
import com.proyect.caseroya.opcionmenu.infrastructure.PerfilOpcionMenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpcionMenuService {

    private final OpcionMenuRepository opcionMenuRepository;
    private final PerfilOpcionMenuRepository perfilOpcionMenuRepository;

    public OpcionMenuService(OpcionMenuRepository opcionMenuRepository,
                             PerfilOpcionMenuRepository perfilOpcionMenuRepository) {
        this.opcionMenuRepository = opcionMenuRepository;
        this.perfilOpcionMenuRepository = perfilOpcionMenuRepository;
    }

    public List<OpcionMenuDto> obtenerMenuPorPerfil(String perfilId) {
        List<String> opcionesIds = perfilOpcionMenuRepository.findByPerfilId(perfilId)
                .stream()
                .map(PerfilOpcionMenu::getOpcionId)
                .collect(Collectors.toList());

        return opcionMenuRepository.findAllById(opcionesIds).stream()
                .filter(o -> Boolean.TRUE.equals(o.getActivo()))
                .map(o -> new OpcionMenuDto(o.getOpcionId(), o.getDescripcion(), o.getUrl(), o.getIcono(), o.getOrden()))
                .collect(Collectors.toList());
    }
}
