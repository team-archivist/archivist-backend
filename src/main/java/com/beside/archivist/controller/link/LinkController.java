package com.beside.archivist.controller.link;

import com.beside.archivist.entity.link.Link;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinkController {
    
    private final LinkService linkServiceImpl;

    @PostMapping("/link")
    public ResponseEntity<?> registerLink(@RequestBody LinkDto linkDto) {
        Link savedLink = linkServiceImpl.saveLink(linkDto);
        return ResponseEntity.ok().body(savedLink);
    }

    @PatchMapping ("/link/{linkId}")
    public ResponseEntity<?> updateLink(@PathVariable("linkId") Long linkId, @RequestBody LinkDto linkDto) {
        Link updatedLink = linkServiceImpl.updateLink(linkId,linkDto);
        return ResponseEntity.ok().body(updatedLink);
    }

    @DeleteMapping("/link/{linkId}")
    public ResponseEntity<?> deleteLink(@PathVariable("linkId") Long linkId){
        linkServiceImpl.deleteLink(linkId);
        return ResponseEntity.ok().body("링크 삭제 완료.");
    }
}
