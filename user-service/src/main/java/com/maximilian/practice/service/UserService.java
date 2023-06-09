package com.maximilian.practice.service;

import com.maximilian.practice.domain.user.UserCreateRequest;
import com.maximilian.practice.model.User;
import com.maximilian.practice.model.UserToUserLink;
import com.maximilian.practice.model.UserToUserLinkId;
import com.maximilian.practice.repository.UserRepository;
import com.maximilian.practice.repository.UserToUserLinkRepository;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserToUserLinkRepository userToUserLinkRepository;

    public User getById(Long id) {
        log.info("Getting user with id '" + id + "'");
        return userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(
                        "Could not find user with id '" + id + "'",
                        HttpStatus.NOT_FOUND
                ));
    }

    public List<User> getAll() {
        log.info("Getting all users...");
        return userRepository.findAll();
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public List<User> getFollowingByUserId(Long id) {
        log.info("Getting content creators, that user with id '" + id + "' follows.");
        // current user is follower, we need to get all content creators of current follower
        return getById(id).getFollowing()
                .stream()
                .map(UserToUserLink::getContentCreator)
                .collect(Collectors.toList());
    }

    public List<User> getFollowersByUserId(Long id) {
        log.info("Getting followers of user with id '" + id + "'.");
        // current user is creator, so we are getting followers
        return getById(id).getFollowers()
                .stream()
                .map(UserToUserLink::getFollower)
                .collect(Collectors.toList());
    }

    public void follow(Long fromId, Long toId) {
        if (fromId.equals(toId)) {
            throw new GeneralException("Cannot follow self.");
        }

        UserToUserLinkId userToUserLinkId = new UserToUserLinkId();
        userToUserLinkId.setFromId(fromId);
        userToUserLinkId.setToId(toId);

        if (userToUserLinkRepository.existsById(userToUserLinkId)) {
            log.info("User #" + fromId + " is already following user #" + toId + "!");
            return;
        }

        UserToUserLink link = new UserToUserLink();
        User follower = getById(fromId);
        User contentCreator = getById(toId);

        log.info("User #" + follower.getId() + " '" + follower.getNickname() +
                "' is trying to follow user #" + contentCreator.getId() +
                " '" + contentCreator.getNickname() + "'");

        link.setId(userToUserLinkId);
        link.setFollower(follower);
        link.setContentCreator(contentCreator);

        userToUserLinkRepository.saveAndFlush(link);
    }

    public void unfollow(Long fromId, Long toId) {
        if (fromId.equals(toId)) {
            throw new GeneralException("Cannot unfollow self.");
        }
        UserToUserLinkId userToUserLinkId = new UserToUserLinkId();
        userToUserLinkId.setFromId(fromId);
        userToUserLinkId.setToId(toId);
        log.info("User #" + fromId + " is trying to unfollow user #" + toId);

        UserToUserLink linkById = getLinkById(userToUserLinkId);
        userToUserLinkRepository.delete(linkById);

    }

    public User create(UserCreateRequest request, Long userId) {
        log.info("Creating user with nickname '" + request.nickname() + "'");
        User newUser = new User();
        newUser.setId(userId);
        newUser.setNickname(request.nickname());
        newUser.setDisplayName(request.displayName());
        newUser.setBio(request.bio());
        newUser.setCreatedAt(Instant.now());
        newUser.setTimezone(ZoneId.systemDefault().getId());
        if (userRepository.existsByNickname(newUser.getNickname())) {
            throw new GeneralException("User '" + newUser.getNickname() + "' already exists.");
        }
        newUser = userRepository.saveAndFlush(newUser);
        return getById(newUser.getId());
    }

    protected UserToUserLink getLinkById(UserToUserLinkId userToUserLinkId) {
        return userToUserLinkRepository.findById(userToUserLinkId)
                .orElseThrow(() -> new GeneralException(
                        "Could not find link " + userToUserLinkId,
                        HttpStatus.NOT_FOUND
                ));
    }

}
