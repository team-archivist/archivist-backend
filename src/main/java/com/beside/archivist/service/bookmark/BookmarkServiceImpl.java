package com.beside.archivist.service.bookmark;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.entity.bookmark.Bookmark;
import com.beside.archivist.repository.bookmark.BookmarkRepository;
import com.beside.archivist.service.group.GroupService;
import com.beside.archivist.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService{
    private final BookmarkRepository bookmarkRepository;
    private final UserService userServiceImpl;
    private final GroupService groupServiceImpl;
    private final AuditConfig auditConfig;
    @Override
    public void saveBookmark(Long groupId) {
        String userEmail = auditConfig.auditorProvider().getCurrentAuditor()
                .orElseThrow(RuntimeException::new); // todo: 인증 X, 예외 처리

        Bookmark bookmark = Bookmark.builder()
                .isOwner(true) // 내가 생성한 그룹
                .users(userServiceImpl.getUserByEmail(userEmail))
                .groups(groupServiceImpl.getGroup(groupId))
                .build();

        bookmark.getUsers().addBookmark(bookmark);
        bookmark.getGroups().addBookmark(bookmark);
        bookmarkRepository.save(bookmark);
    }
}
