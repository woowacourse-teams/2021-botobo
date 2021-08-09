import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import EditIcon from '../assets/pencil.svg';
import { Button, InputField, MainHeader, TextAreaField } from '../components';
import {
  BIO_MAXIMUM_LENGTH,
  CLOUD_FRONT_DOMAIN,
  USER_NAME_MAXIMUM_LENGTH,
} from '../constants';
import { FormProvider } from '../contexts';
import { useProfile, useSnackbar } from '../hooks';
import { Flex } from '../styles';

interface ImageEditorStyleProp {
  isEditable: boolean;
}

const userSrc = `${CLOUD_FRONT_DOMAIN}/user.png`;

const validateFileSize = (size: number) => {
  const mb = 1024 * 1024;

  if (size < 0 || size > 10 * mb) {
    throw new Error('파일 크기는 10MB를 초과할 수 없어요.');
  }
};

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
  const { userInfo, updateProfileUrl, editProfile } = useProfile();
  const [isEditable, setIsEditable] = useState(false);

  const showSnackbar = useSnackbar();

  const uploadImage: React.ChangeEventHandler<HTMLInputElement> = ({
    target,
  }) => {
    setIsEditable(false);
    const file = target.files?.[0];

    if (!file) return;

    const size = file.size ?? 0;

    try {
      validateFileSize(size);
      updateProfileUrl(file);
    } catch (error) {
      showSnackbar({ message: error.message });
    }
  };

  return (
    <>
      <MainHeader />
      <Container>
        <Profile>
          <ImageContainer>
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
              <ImageUploader htmlFor="image-upload">
                <input
                  type="file"
                  id="image-upload"
                  name="image-upload"
                  accept="image/png, image/jpg, image/jpeg, image/bmp"
                  onChange={uploadImage}
                />
                <span>이미지 업로드</span>
              </ImageUploader>
              <button
                type="button"
                role="image-delete-button"
                onClick={() => {
                  updateProfileUrl();
                  setIsEditable(false);
                }}
              >
                이미지 제거
              </button>
            </ImageEditor>
          </ImageContainer>
          <FormProvider
            initialValues={{
              userName: userInfo?.userName ?? 'Unknown User',
              bio: userInfo?.bio ?? '',
            }}
            validators={{ userName: validateUserName, bio: validateBio }}
            onSubmit={({ userName, bio }) => {
              if (!userInfo) return;

              editProfile({ ...userInfo, userName, bio });
            }}
          >
            <InputTitle>이름</InputTitle>
            <NameInput
              name="userName"
              focusColor="gray"
              maxLength={USER_NAME_MAXIMUM_LENGTH}
            />
            <InputTitle>소개글</InputTitle>
            <BioInput
              name="bio"
              focusColor="gray"
              placeholder="소개글을 작성해주세요."
              maxLength={BIO_MAXIMUM_LENGTH}
            />
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
  width: 100%;
  height: 37.5rem;
  padding: 2rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.card};
  `};
`;

const ImageContainer = styled.div`
  position: relative;
`;

const ImageWrapper = styled.div`
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
  position: absolute;
  top: 5.5rem;
  right: -4.5rem;
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

  &::before {
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

  & > * {
    line-height: 1.5;
  }
`;

const ImageUploader = styled.label`
  & > input {
    display: none;
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

const BioInput = styled(TextAreaField)`
  width: 100%;
  height: 5rem;
  padding: 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    border-radius: ${theme.borderRadius.square};
    border: 1px solid ${theme.color.gray_4};
  `}
`;

export default ProfilePage;
