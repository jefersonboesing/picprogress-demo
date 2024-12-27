package com.picprogress.usecase

import com.picprogress.model.Result
import com.picprogress.model.album.Album
import com.picprogress.model.photo.AlbumPhoto
import com.picprogress.model.photo.PhotoPath
import com.picprogress.repository.AlbumsRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue


class AddPhotoUseCaseTest {

    private val repository = mock<AlbumsRepository>()
    private val useCase = AddPhotoUseCase(repository)

    @Test
    fun `when calls invoke returns success`() = runBlocking {
        // given
        val album = Album()
        val photoPath = PhotoPath("/path/to/photo")
        val albumPhoto = AlbumPhoto(album = album, photoPath = photoPath)
        everySuspend { repository.addPhoto(album, photoPath) } returns Unit

        // when
        val result = useCase.invoke(albumPhoto)

        // then
        verifySuspend { repository.addPhoto(album, photoPath) }
        assertTrue(result is Result.Success)
    }

    @Test
    fun `when calls invoke returns failure`() = runBlocking {
        // given
        val album = Album()
        val photoPath = PhotoPath("/path/to/photo")
        val albumPhoto = AlbumPhoto(album = album, photoPath = photoPath)
        everySuspend { repository.addPhoto(album, photoPath) } throws Exception()

        // when
        val result = useCase.invoke(albumPhoto)

        // then
        verifySuspend { repository.addPhoto(album, photoPath) }
        assertTrue(result is Result.Failure)
    }

}
