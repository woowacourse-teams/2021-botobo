import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useRecoilState } from 'recoil';

import EditIcon from '../assets/pencil.svg';
import { Button, InputField, MainHeader } from '../components';
import {
  BIO_MAXIMUM_LENGTH,
  CLOUD_FRONT_DOMAIN,
  USER_NAME_MAXIMUM_LENGTH,
} from '../constants';
import { FormProvider } from '../contexts';
import { userState } from '../recoil';
import { Flex } from '../styles';

interface ImageEditorStyleProp {
  isEditable: boolean;
}

const userSrc = `${CLOUD_FRONT_DOMAIN}/user.png`;

const validateUserName = (value: string) => {
  if (value.length > USER_NAME_MAXIMUM_LENGTH) {
    throw new Error(`닉네임은 ${USER_NAME_MAXIMUM_LENGTH}자 이하여야 합니다.`);
  }
};

const validateBio = (value: string) => {
  if (value.length > BIO_MAXIMUM_LENGTH) {
    throw new Error(`소개글은 ${BIO_MAXIMUM_LENGTH}자 이하여야 합니다.`);
  }
};

const ProfilePage = () => {
  const [userInfo] = useRecoilState(userState);
  const [isEditable, setIsEditable] = useState(false);

  return (
    <>
      <MainHeader />
      <Container>
        <Profile>
          <ImageWrapper
            onClick={() => setIsEditable((prevState) => !prevState)}
          >
            <Avatar src={userInfo?.profileUrl ?? userSrc} />
            <EditIconWrapper>
              <EditIcon width="1.3rem" height="1.3rem" />
            </EditIconWrapper>
          </ImageWrapper>
          {isEditable && <Dimmed onClick={() => setIsEditable(false)} />}
          <ImageEditor isEditable={isEditable}>
            <button>이미지 업로드</button>
            <button>이미지 제거</button>
          </ImageEditor>
          <FormProvider
            initialValues={{
              name: userInfo?.userName ?? 'Unknown User',
              bio: '',
            }}
            validators={{ name: validateUserName, bio: validateBio }}
            onSubmit={({ name, bio }) => console.log(name, bio)}
          >
            <InputTitle>이름</InputTitle>
            <NameInput
              name="name"
              focusColor="gray"
              maxLength={USER_NAME_MAXIMUM_LENGTH}
            />
            <InputTitle>소개글</InputTitle>
            <BioInput name="bio"></BioInput>
            <Button size="full">프로필 수정</Button>
          </FormProvider>
        </Profile>
      </Container>
    </>
  );
};

const Container = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      height: ${theme.pageSize.height};
    `}
`;

const Profile = styled.div`
  ${Flex({ direction: 'column', items: 'center' })};
  position: relative;
  width: 100%;
  height: 37.5rem;
  padding: 2rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.card};
  `};
`;

const ImageWrapper = styled.div`
  position: relative;
  cursor: pointer;
`;

const Avatar = styled.img`
  width: 10rem;
  height: 10rem;
  margin: 2.5rem 0;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.circle};
  `}
`;

const EditIconWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  position: absolute;
  top: 2.5rem;
  right: 0;
  width: 2.5rem;
  height: 2.5rem;

  ${({ theme }) => css`
    border: 1px solid ${theme.color.gray_4};
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.circle};
  `};
`;

const Dimmed = styled.div`
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
`;

const ImageEditor = styled.div<ImageEditorStyleProp>`
  ${Flex({
    direction: 'column',
    justify: 'space-around',
    items: 'flex-start',
  })};
  position: absolute;
  top: 7.5rem;
  right: 0.8rem;
  width: 7rem;
  height: 4rem;
  padding: 0.5rem;
  z-index: 2;

  ${({ theme, isEditable }) => css`
    border: 1px solid ${theme.color.gray_4};
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.button};
    display: ${isEditable ? 'block' : 'none'};
  `};

  &:before {
    display: inline-block;
    position: absolute;
    left: 9px;
    top: -16px;
    content: '';
    border: 8px solid transparent;

    ${({ theme }) => css`
      border-bottom: 8px solid ${theme.color.white};
    `}
  }

  & > button {
    display: block;
    line-height: 1.5;
  }
`;

const InputTitle = styled.div`
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    font-weight: ${theme.fontWeight.bold};
  `}
`;

const NameInput = styled(InputField)`
  width: 100%;
  padding: 0.3rem;
  outline: none;
  border: none;
  background-color: transparent;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    border-bottom: 1px solid ${theme.color.gray_5};
  `}
`;

const BioInput = styled.textarea`
  width: 100%;
  height: 5rem;
  margin-bottom: 2rem;
  padding: 0.5rem;
  outline: none;
  resize: none;
  overflow-y: auto;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    border: 1px solid ${theme.color.gray_5};
    border-radius: ${theme.borderRadius.square};

    &:focus {
      border: 1px solid ${theme.color.gray_8};
    }
  `}
`;

export default ProfilePage;
